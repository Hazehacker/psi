package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PaymentFormFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PaymentFormQuery;

public interface PaymentFormService {
    /**
     * 分页查询付款单列表
     *
     * @param query 查询参数
     * @return 付款单列表
     */
    PageDTO<PaymentFormFormDTO> listPaymentForm(PaymentFormQuery query);

    /**
     * 导出付款单数据到Excel
     *
     * @param query    查询参数
     * @return Excel文件字节数组
     */
    byte[] exportPaymentFormToExcel(PaymentFormQuery query);
}
