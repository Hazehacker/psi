package com.zeroone.star.purchase.service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 采购订单报表导出接口
 * @author: 加减法
 */
public interface IPurchaseOrderReportService {

    /**
     * 导出采购订单详细报表
     * @param ids 要导出的采购订单ID列表
     * @return 包含Excel数据的字节数组
     */
    public byte[] exportPurchaseOrderReport(List<String> ids);
}
