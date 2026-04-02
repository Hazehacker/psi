package com.zeroone.star.project.j5.procurementreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PaymentFormFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PaymentFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 采购付款表单Api
 */
public interface PaymentFormApi {
    /**
     * 查询采购付款表单列表
     *
     * @param query 查询参数
     * @return 采购付款表单列表
     */
    JsonVO<PageDTO<PaymentFormFormDTO>> listPaymentForm(PaymentFormQuery query);

    /**
     * 导出采购付款表单列表
     *
     * @param query 查询参数
     * @return 采购付款表单列表
     */
    ResponseEntity<byte[]> exportPaymentFormToExcel(PaymentFormQuery query);
}
