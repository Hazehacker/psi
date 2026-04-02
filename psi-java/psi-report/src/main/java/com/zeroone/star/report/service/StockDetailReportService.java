package com.zeroone.star.report.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.query.j8.report.StockDetailReportQuery;
import com.zeroone.star.project.vo.j8.report.StockDetailReportVO;

import javax.servlet.http.HttpServletResponse;

public interface StockDetailReportService {

    PageDTO<StockDetailReportVO> listStockDetail(StockDetailReportQuery stockDetailReportQuery);

    void exportDetailData(StockDetailReportQuery query, HttpServletResponse response);
}
