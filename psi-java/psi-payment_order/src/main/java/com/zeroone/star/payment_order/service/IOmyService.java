package com.zeroone.star.payment_order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.payment_order.entity.OmyDO;
import com.zeroone.star.project.dto.j6.payment_order.OmyImportDTO;
import com.zeroone.star.project.vo.j6.payment_order.OmyInfoDetailVO;
import com.zeroone.star.project.vo.j6.payment_order.OmySimpleVO;

import java.util.List;

/**
 * 付款单Service接口
 */
public interface IOmyService extends IService<OmyDO> {
    /**
     * 批量导入付款单详情（仅导入omy_info表）
     * @param infoList 付款单详情列表
     * @return 导入成功数量
     */
    int batchImportOmyInfo(List<OmyImportDTO> infoList);

    /**
     * 查询付款单关联核销单（用于导出简单报表）
     * @return 付款单简单报表VO列表
     */
    List<OmySimpleVO> listAllForSimpleReport();

    /**
     * 查询所有付款单详情（用于导出详细报表）
     * @return 付款单详情报表VO列表
     */
    List<OmyInfoDetailVO> listAllForDetailReport();
}
