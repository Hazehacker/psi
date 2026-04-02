package com.zeroone.star.finance.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zeroone.star.finance.entity.Oce;
import com.zeroone.star.finance.mapper.OceBillMapper;
import com.zeroone.star.finance.mapper.OceInfoMapper;
import com.zeroone.star.finance.mapper.OceMapper;
import com.zeroone.star.finance.service.IOceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 其它支出单 服务实现类
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Service
@Transactional
public class OceServiceImpl extends ServiceImpl<OceMapper, Oce> implements IOceService {

    @Resource
    private OceMapper oceMapper;

    @Resource
    private OceBillMapper oceBillMapper;

    @Resource
    private OceInfoMapper oceInfoMapper;

    /**
     * 删除其他的支出单
     * @param ids 支出单id集合
     * @return JsonVO<String>
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务注解：异常时回滚所有操作
    public JsonVO<String> deleteOtherExpenseForm(List<String> ids) {
        // 入参校验：ids为空直接返回失败
        if (CollectionUtils.isEmpty(ids)) {
            log.warn("批量删除其他支出单：入参ids为空");
            return JsonVO.fail("删除失败：请选择要删除的支出单");
        }

        try {
            List<String> notDeleteIds = new ArrayList<>();

            // 1. 校验：已审核（examine=1）的单据不允许删除
            for (String id : ids) {
                // 优化：仅查询examine字段，减少IO开销（对应Mapper方法需修改）
                Integer examineStatus = oceMapper.selectExamineStatusById(id);
                // 若状态为1（已审核）或查询不到记录（id无效），加入不可删除列表
                if (examineStatus != null && examineStatus == 1) {
                    notDeleteIds.add(id);
                } else if (examineStatus == null) {
                    notDeleteIds.add(id);
                }
            }

            // 2. 存在不可删除的单据，返回失败信息
            if (!CollectionUtils.isEmpty(notDeleteIds)) {
                String errorMsg = String.format("删除失败：以下单据已审核或不存在，无法删除：%s", notDeleteIds);
                return JsonVO.fail(errorMsg);
            }

            // 3. 先删除子表数据（主子表关联，需先删子表）
            oceBillMapper.deleteByPids(ids); // 删除oce_bill子表（关联主表pid）
            oceInfoMapper.deleteByPids(ids); // 删除oce_info子表（关联主表pid）

            // 4. 再删除主表数据
            int deleteCount = oceMapper.deleteByIds(ids);


            return JsonVO.success("批量删除其他支出单成功，共删除" + deleteCount + "条记录");

        } catch (Exception e) {
            return JsonVO.fail("批量删除失败：" + e.getMessage());
        }
    }
}
