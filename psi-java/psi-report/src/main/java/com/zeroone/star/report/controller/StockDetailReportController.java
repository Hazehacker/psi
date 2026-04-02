package com.zeroone.star.report.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.j8.report.StockDetailReportApis;
import com.zeroone.star.project.query.j8.report.StockDetailReportQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j8.report.StockDetailReportVO;
import com.zeroone.star.report.service.StockDetailReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/report/detail")
@Api(tags = "报表 - 商品收发明细")
public class StockDetailReportController implements StockDetailReportApis {

    private final StockDetailReportService stockDetailReportService;

    public StockDetailReportController(StockDetailReportService stockDetailReportService) {
        this.stockDetailReportService = stockDetailReportService;
    }

    @GetMapping("/list")
    @ApiOperation("获取商品收发明细列表")
    public JsonVO<PageDTO<StockDetailReportVO>> getDetailList(StockDetailReportQuery query) {
        return JsonVO.success(stockDetailReportService.listStockDetail(query));
    }

    @GetMapping("/export")
    @ApiOperation("导出商品收发明细数据")
    public void exportDetailData(StockDetailReportQuery stockDetailReportQuery, HttpServletResponse response) {
        stockDetailReportService.exportDetailData(stockDetailReportQuery, response);
    }
}
