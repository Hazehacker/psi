package com.zeroone.star.payment_order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.payment_order.mapper.*;
import com.zeroone.star.payment_order.service.IOmyService;
import com.zeroone.star.project.dto.j6.payment_order.OmyImportDTO;
import com.zeroone.star.payment_order.entity.OmyDO;
import com.zeroone.star.payment_order.entity.OmyInfoDO;
import com.zeroone.star.project.vo.j6.payment_order.OmyInfoDetailVO;
import com.zeroone.star.project.vo.j6.payment_order.OmySimpleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 付款单Service实现类
 */
@Service
public class OmyServiceImpl extends ServiceImpl<OmyMapper2, OmyDO> implements IOmyService {

    @Resource(name = "omyInfoMapper2")
    private OmyInfoMapper2 omyInfoMapper2;

    @Resource(name = "omyBillMapper2")
    private OmyBillMapper2 omyBillMapper2;

    /**
     * 批量导入付款单详情（仅导入omy_info表）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchImportOmyInfo(List<OmyImportDTO> infoList) {
        int successCount = 0;
        for (OmyImportDTO infoDTO : infoList) {
            // 1. 生成主键ID（使用UUID确保唯一性）
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            infoDTO.setId(id);

            // 2. 生成pid（用UUID生成唯一值）
            String pid = UUID.randomUUID().toString().replaceAll("-", "");
            infoDTO.setPid(pid); // 赋值给DTO的pid字段

            // 3. 转换DTO为DO并插入数据库
            OmyInfoDO infoDO = new OmyInfoDO();
            BeanUtils.copyProperties(infoDTO, infoDO);
            omyInfoMapper2.insert(infoDO);

            successCount++;
        }
        return successCount;
    }

    /**
     * 查询付款单关联核销单（用于导出简单报表）
     */
    @Override
    public List<OmySimpleVO> listAllForSimpleReport() {
        List<OmyDO> omyList = baseMapper.selectList(null);
        List<OmySimpleVO> voList = new ArrayList<>();

        for (OmyDO omyDO : omyList) {
            OmySimpleVO vo = new OmySimpleVO();
            BeanUtils.copyProperties(omyDO, vo);

            // 转换审核状态为文本
            vo.setExamineDesc(omyDO.getExamine() == 0 ? "未审核" : "已审核");
            // 转换核销状态为文本
            switch (omyDO.getNucleus()) {
                case 0:
                    vo.setNucleusDesc("未核销");
                    break;
                case 1:
                    vo.setNucleusDesc("部分核销");
                    break;
                case 2:
                    vo.setNucleusDesc("已核销");
                    break;
                default:
                    vo.setNucleusDesc("未知");
            }

            // 查询该付款单的核销金额总和
            BigDecimal writeOffAmount = omyBillMapper2.sumMoneyByPid(omyDO.getId());
            vo.setWriteOffAmount(writeOffAmount == null ? BigDecimal.ZERO : writeOffAmount);

            voList.add(vo);
        }
        return voList;
    }

    /**
     * 查询所有付款单详情（用于导出详细报表）
     */
    @Override
    public List<OmyInfoDetailVO> listAllForDetailReport() {
        // 1. 查询所有付款单ID
        List<OmyDO> omyList = baseMapper.selectList(null);
        List<OmyInfoDetailVO> voList = new ArrayList<>();

        // 2. 逐个查询详情并转换
        for (OmyDO omyDO : omyList) {
            List<OmyInfoDO> infoList = omyInfoMapper2.selectByPid(omyDO.getId());
            for (OmyInfoDO infoDO : infoList) {
                OmyInfoDetailVO vo = new OmyInfoDetailVO();
                BeanUtils.copyProperties(infoDO, vo);
                voList.add(vo);
            }
        }
        return voList;
    }
}
