package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.Bre;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j1.homepage.PurchaseReturnDailyTotalResponseDTO;
import com.zeroone.star.project.query.j1.homepage.PurchaseReturnDailyTotalQuery;

/**
 * <p>
 * 采购退货单 服务类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
public interface IBreService extends IService<Bre> {

    /**
     * 查询采购单退货单汇总
     * @param query
     * @return
     */
    PurchaseReturnDailyTotalResponseDTO queryPurchaseReturnDailyTotal(PurchaseReturnDailyTotalQuery query);
}
