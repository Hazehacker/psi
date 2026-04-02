package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.ProfitReportDTO;
import com.zeroone.star.project.query.j5.fundreport.ProfitQuery;

public interface ProfitService {
    /**
     * 获取利润表报表（条件+分页）
     *
     * @param query 查询参数
     * @return 利润表报表分页数据
     */
    PageDTO<ProfitReportDTO> listProfitReportForm(ProfitQuery query);

    /**
     * 导出利润表数据到Excel
     *
     * @param query 查询参数
     * @return Excel文件字节数组
     */
    byte[] exportProfitReportToExcel(ProfitQuery query);
}
