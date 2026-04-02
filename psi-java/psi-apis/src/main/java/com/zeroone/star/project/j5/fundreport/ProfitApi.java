package com.zeroone.star.project.j5.fundreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.ProfitReportDTO;
import com.zeroone.star.project.query.j5.fundreport.ProfitQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 利润表Api
 */
public interface ProfitApi {
    /**
     * 查询利润表信息
     */
    JsonVO<PageDTO<ProfitReportDTO>> listProfitReportForm(ProfitQuery query);

    /**
     * 导出利润表信息到Excel
     */
    ResponseEntity<byte[]> exportProfitReportToExcel(ProfitQuery query);

}
