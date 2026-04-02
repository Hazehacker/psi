package com.zeroone.star.reportmanagement.mapper.procurementreport;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderSummaryFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderSummaryFormQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 采购汇总表 Mapper
 */
@Mapper
public interface PurchaseOrderSummaryFormMapper extends BaseMapper<PurchaseOrderSummaryFormDTO> {

    /**
     * 查询采购汇总信息
     *
     * @param query 查询条件
     * @return 汇总结果列表
     */
    Page<PurchaseOrderSummaryFormDTO> listPurchaseSummary(
            @Param("page") Page<PurchaseOrderSummaryFormDTO> page,
            @Param("query") PurchaseOrderSummaryFormQuery query);
}
