package com.zeroone.star.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnGoodsDetailDTO;
import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnListVO;
import com.zeroone.star.purchase.DO.BreBillDO;
import com.zeroone.star.purchase.DO.BreDO;
import com.zeroone.star.purchase.mapper.BreBillMapper;
import com.zeroone.star.purchase.service.IBreBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购退货单核销详情 服务实现类
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
@Service
public class BreBillServiceImpl extends ServiceImpl<BreBillMapper, BreBillDO> implements IBreBillService {

    /**
     * @param breDOList
     * @return
     * @author Hazenix 
     * 根据采购退货单号查询核销详情并封装BreVOPage
     */
    @Override
    public List<PurchaseReturnListVO> getBreVOList(List<BreDO> breDOList) {
        // 1.批量查询核销金额（用source关联）
        List<String> sources = breDOList.stream().map(BreDO::getSource)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        Map<String, BigDecimal> writeoffMap = new HashMap<>();

        // 2.聚合查询核销金额
        if (!sources.isEmpty()) {
            // 用 QueryWrapper 来构造 SQL
            QueryWrapper<BreBillDO> wrapper = new QueryWrapper<>();
            wrapper.select("pid", "SUM(money) AS total_writeoff")
                    .in("pid", sources)
                    .groupBy("pid");
            // 用 this.listMaps(wrapper) 执行
            List<Map<String, Object>> writeoffList = this.listMaps(wrapper);

            // 3.转成Map<pid,toal_writeoff>
            for (Map<String, Object> map : writeoffList) {
                String pid = (String) map.get("pid");
                BigDecimal total = (BigDecimal) map.get("total_writeoff");
                writeoffMap.put(pid, total);
            }
        }

        // 4.封装 VO 列表
        List<PurchaseReturnListVO> purchaseReturnListVOList = breDOList.stream().map(breDO -> {
            PurchaseReturnListVO purchaseReturnListVO = new PurchaseReturnListVO();

            // 4.1复制基础字段
            BeanUtils.copyProperties(breDO, purchaseReturnListVO);

            // 4.2设置核销金额
            BigDecimal writeoff = writeoffMap.get(breDO.getSource());
            purchaseReturnListVO.setWriteOffAmount(writeoff);

            return purchaseReturnListVO;
        }).collect(Collectors.toList());


        // 6.返回数据
        return purchaseReturnListVOList;
    }
}
