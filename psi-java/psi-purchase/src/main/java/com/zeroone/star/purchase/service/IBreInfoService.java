package com.zeroone.star.purchase.service;

import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnAddDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnGoodsDetailDTO;
import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnDetailVO;
import com.zeroone.star.purchase.DO.BreInfoDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 采购退货单详情 服务类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
public interface IBreInfoService extends IService<BreInfoDO> {

    /**
     * @auther xiaoliu
     * 获取取指定采购退货单详情VO
     * @param id
     * @return
     */
    PurchaseReturnDetailVO getPurchaseReturnDetailVO(String id);
}
