package com.zeroone.star.purchase.service;

import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnListVO;
import com.zeroone.star.purchase.DO.BreBillDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.purchase.DO.BreDO;

import java.util.List;

/**
 * <p>
 * 采购退货单核销详情 服务类
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
public interface IBreBillService extends IService<BreBillDO> {

    /**
     * @param breDOList
     * @return
     * @author Hazenix 
     * 根据采购退货单号查询核销详情并封装BreVOPage
     */
    List<PurchaseReturnListVO> getBreVOList(List<BreDO> breDOList);

}
