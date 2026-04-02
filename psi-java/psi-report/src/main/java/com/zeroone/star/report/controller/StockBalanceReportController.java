package com.zeroone.star.report.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.report.ProductStockBalanceDTO;
import com.zeroone.star.project.j8.report.StockBalanceReportApis;
import com.zeroone.star.project.query.j8.report.ProductStockBalanceQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.report.service.StockBalanceReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 库存余额 控制器类
 * <p>
 * autor:冯烨
 */
@RestController
@RequestMapping("/report/balance")
@Api(tags = "报表 - 库存余额管理")
public class StockBalanceReportController implements StockBalanceReportApis {
    @Resource
    private StockBalanceReportService service;

    @ApiOperation("查询库存余额")
    @GetMapping("/select")
    public JsonVO<PageDTO<ProductStockBalanceDTO>> getProductStocksByCondition(ProductStockBalanceQuery query) {
        return JsonVO.success(service.getProductStocksByCondition(query));
    }

    @ApiOperation("导出库存余额（按当前查询条件导出全部）")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public ResponseEntity<byte[]> export(ProductStockBalanceQuery query) {
        return service.export(query);
    }
}
