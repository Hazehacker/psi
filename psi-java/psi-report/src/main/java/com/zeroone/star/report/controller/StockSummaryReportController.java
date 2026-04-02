package com.zeroone.star.report.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.report.wss_KazamataNeri.StockSummaryReportDTO;
import com.zeroone.star.project.j8.report.StockSummaryReportApis;
import com.zeroone.star.project.query.j8.report.StockSummaryReportQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.report.service.StockSummaryReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/report/summary")
@Api(tags = "报表 - 商品收发汇总表")
public class StockSummaryReportController implements StockSummaryReportApis {

    @Resource
    StockSummaryReportService stockSummaryReportService;

    /**
     * 查询商品收发汇总表
     *
     * @param query 条件查询
     * @return 查询结果
     */
    @GetMapping("/list")
    @ApiOperation("查询商品收发汇总表")
    public JsonVO<PageDTO<StockSummaryReportDTO>> listGoodsReceiptAndDispatchSummary(StockSummaryReportQuery query) {
        log.info("查询商品收发汇总表: {}", query);
        PageDTO<StockSummaryReportDTO> result = stockSummaryReportService.listGoodsReceiptAndDispatchSummary(query);
        return JsonVO.success(result);
    }

    /**
     * 导出商品收发汇总表报表
     *
     * @param query 导出查询参数
     * @return 报表
     */
    @SneakyThrows
    @GetMapping(value = "/export", produces = "application/octet-stream")
    @ApiOperation("导出商品收发汇总表报表")
    public ResponseEntity<byte[]> exportWssExcel(StockSummaryReportQuery query) {
        return stockSummaryReportService.exportWssExcel(query);
    }
}
