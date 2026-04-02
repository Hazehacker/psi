package com.zeroone.star.payment_order.service;

import com.zeroone.star.payment_order.entity.Supplier;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 供应商 服务类
 * </p>
 *
 * @author 温白开
 * @since 2025-10-28
 */
public interface SupplierService extends IService<Supplier> {
    //减少应付款
    void updatePayableReduce(String sourceId);
    //恢复应付款
    void updatePayableRestore(String omyId);

    //减少应付款
    void updatePayableReduce(String omyId, BigDecimal totalAmount);
    //恢复应付款
    void updatePayableRestore(String omyId, BigDecimal totalAmount);

}
