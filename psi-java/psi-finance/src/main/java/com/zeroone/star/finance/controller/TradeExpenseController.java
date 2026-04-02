package com.zeroone.star.finance.controller;

import com.zeroone.star.finance.service.tradeexpense.TradeExpenseService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.CostDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.TradeExpenseReportDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.TradeExpenseResponseDTO;
import com.zeroone.star.project.j8.finance.TradeExpenseApis;
import com.zeroone.star.project.query.j8.finance.tradeexpense.TradeExpenseReportQuery;
import com.zeroone.star.project.query.j8.finance.tradeexpense.TradeExpenseQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购销费用接口
 * ---------------------------------
 * 功能模块：
 * 1. 获取费用列表（支持条件查询 + 分页）
 * 2. 获取结算单数据
 * 3. 导出费用数据（Excel/CSV）
 * 4. 获取购销费用报表（支持条件查询 + 分页）
 * 5. 导出报表数据（Excel/CSV）
 */
@RestController
@RequestMapping("/finance/trade-expense")
@Api(tags = "资金管理 - 购销费用接口")
public class TradeExpenseController implements TradeExpenseApis {

    @Resource
    private TradeExpenseService tradeExpenseService;

    /**
     * 1. 获取费用列表（支持条件查询 + 分页）
     *
     * @param query 条件查询对象
     * @return 查询结果
     */
    @GetMapping(value = "/query/page")
    @ApiOperation(value = "查询购销费用分页列表")
    public JsonVO<PageDTO<TradeExpenseResponseDTO>> queryTradeExpensePage(TradeExpenseQuery query) {
        return JsonVO.success(tradeExpenseService.queryTradeExpensePage(query));
    }

    /**
     * 2.获取结算单数据
     *
     * @param ids 操作id集合
     * @return 结算单数据详情
     */
    @GetMapping("/getSettles")
    @ApiOperation("获取结算单数据")
    public JsonVO<List<CostDTO>> getSettles(@RequestParam List<String> ids) {
        return JsonVO.success(tradeExpenseService.getSettles(ids));
    }


    /**
     * 3.导出费用数据（Excel/CSV）
     *
     * @return 报表
     */
    @GetMapping(value = "/export/expense", produces = "application/octet-stream")
    @ApiOperation("导出数据(购销费用)")
    public ResponseEntity<byte[]> exportBillExcel(
            @ApiParam(value = "cost的id集合") @RequestParam List<String> ids) {
        return tradeExpenseService.exportBillExcel(ids);
    }

    /**
     * 4.获取购销费用报表（支持条件查询 + 分页）
     *
     * @param query 查询参数
     * @return 购销费用列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "购销费用报表(条件+分页)")
    public JsonVO<PageDTO<TradeExpenseReportDTO>> listBill(TradeExpenseReportQuery query) {
        return JsonVO.success(tradeExpenseService.queryTradeExpenseReportPage(query));
    }


    /**
     * 5.导出报表数据
     *
     * @return 报表
     */
    @GetMapping("/export/report")
    @ApiOperation("导出报表数据")
    public ResponseEntity<byte[]> exportReportData(TradeExpenseReportQuery query) {
        return tradeExpenseService.exportReportData(query);
    }
}
