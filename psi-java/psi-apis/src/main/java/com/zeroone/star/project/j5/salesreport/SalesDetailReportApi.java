package com.zeroone.star.project.j5.salesreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesDetailFormDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesDetailFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 销售明细报表Api
 * @author rainsilent
 * @data 2025/10/19
 */
public interface SalesDetailReportApi {
    /**
     * 查询销售明细表
     * @param query 查询参数
     * @return 销售明细表数据
     */
    JsonVO<PageDTO<SalesDetailFormDTO>> listSalesDetailForm(SalesDetailFormQuery query);

    /**
     * 导出销售明细表为Excel
     * @param query 查询参数
     * @return 销售明细表
     */
    ResponseEntity<byte[]> exportSalesDetailFormToExcel(SalesDetailFormQuery query);
}
