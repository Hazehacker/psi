package com.zeroone.star.reportmanagement.mapper.procurementreport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.procurementreport.PaymentFormFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PaymentFormQuery;
import org.apache.ibatis.annotations.Param;

/**
 * @author heimenkyou
 * @description 针对表【buy(采购单)】的数据库操作Mapper
 * @createDate 2025-10-28 15:10:45
 * @Entity com.zeroone.star.reportmanagement.entity.Buy
 */
public interface PurchasePaymentReportMapper {

    /**
     * 查询采购付款单列表
     * @param query 查询参数
     * @param page 分页对象
     * @return 采购付款单列表
     */
    Page<PaymentFormFormDTO> selectPurchasePaymentList(
            Page<PaymentFormFormDTO> page,
            @Param("query") PaymentFormQuery query
    );
}
