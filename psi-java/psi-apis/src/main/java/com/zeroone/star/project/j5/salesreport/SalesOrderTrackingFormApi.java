package com.zeroone.star.project.j5.salesreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesOrderTrackingFormDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesOrderTrackingFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 销售订单跟踪表Api
 * 
 * @author leyu
 * @date 2025-10-19
 */
public interface SalesOrderTrackingFormApi {

    /**
     * 查询销售订单跟踪表
     * @param query 查询参数
     * @return 销售订单跟踪表
     */
    JsonVO<PageDTO<SalesOrderTrackingFormDTO>> listSalesOrderTrackingForm(SalesOrderTrackingFormQuery query);

    /**
     * 导出销售订单跟踪表
     * @param query 查询参数
     * @return 销售订单跟踪表下载文件
     */
    ResponseEntity<byte[]> exportSalesOrderTrackingFormToExcel(SalesOrderTrackingFormQuery query);

}
