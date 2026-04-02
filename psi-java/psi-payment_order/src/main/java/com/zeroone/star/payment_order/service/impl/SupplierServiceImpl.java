package com.zeroone.star.payment_order.service.impl;

import com.zeroone.star.payment_order.config.BusinessException;
import com.zeroone.star.payment_order.entity.Omy;
import com.zeroone.star.payment_order.entity.Supplier;
import com.zeroone.star.payment_order.mapper.OmyMapper;
import com.zeroone.star.payment_order.mapper.SupplierMapper;
import com.zeroone.star.payment_order.service.SupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * <p>
 * 供应商 服务实现类
 * </p>
 *
 * @author 温白开
 * @since 2025-10-28
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {
    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private OmyMapper omyMapper;


    /**
     * 审核 (扣减供应商应付款余额)
     * @param omyId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePayableReduce(String omyId) {
        Omy omy = omyMapper.selectById(omyId);
        int rows = supplierMapper.updatePayable(omy.getSupplier(),omy.getTotal() );
        if (rows == 0){
            throw new BusinessException("扣除供应商应付款失败");}
    }

    /**
     * 反审核 (恢复供应商应付款余额)
     * @param omyId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePayableRestore(String omyId) {
        Omy omy = omyMapper.selectById(omyId);
            int rows = supplierMapper.updatePayableAdd(omy.getSupplier(), omy.getTotal());
            if (rows == 0){
                throw new BusinessException("恢复供应商应付款失败");}

    }

    /**
     * 审核(批量扣减供应商应付款余额)
     * @param omyId
     * @param totalAmount
     */
    @Override
    public void updatePayableReduce(String omyId, BigDecimal totalAmount) {
        Omy omy = omyMapper.selectById(omyId);
        int rows = supplierMapper.updatePayable(omy.getSupplier(), totalAmount);
        if (rows == 0) {
            throw new BusinessException("扣除供应商应付款失败");
        }
    }

    /**
     * 反审核(批量恢复供应商应付款余额)
     * @param omyId
     * @param totalAmount
     */
    @Override
    public void updatePayableRestore(String omyId, BigDecimal totalAmount) {
        Omy omy = omyMapper.selectById(omyId);
        int rows = supplierMapper.updatePayableAdd(omy.getSupplier(), totalAmount);
        if (rows == 0) {
            throw new BusinessException("恢复供应商应付款失败");
        }
    }
}
