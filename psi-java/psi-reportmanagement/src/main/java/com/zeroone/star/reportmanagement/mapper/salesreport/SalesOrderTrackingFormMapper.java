package com.zeroone.star.reportmanagement.mapper.salesreport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderTrackingFormDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesOrderTrackingFormDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesOrderTrackingFormQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售订单跟踪表Mapper
 *
 * @author leyu
 * @date 2025-10-23
 */

@Mapper
public interface SalesOrderTrackingFormMapper {


    List<SalesOrderTrackingFormDTO> selectSalesOrderTrackingFormByGoods(@Param("query") SalesOrderTrackingFormQuery query);

    List<SalesOrderTrackingFormDTO> selectSalesOrderTrackingForm(@Param("query") SalesOrderTrackingFormQuery query);

    List<SalesOrderTrackingFormDTO> selectAllForExportByGoods(@Param("query") SalesOrderTrackingFormQuery query);

    List<SalesOrderTrackingFormDTO> selectAllForExport(@Param("query") SalesOrderTrackingFormQuery query);
}
