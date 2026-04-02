package com.zeroone.star.finance.controller;

import com.zeroone.star.finance.service.tradeinvoice.IInvoiceService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.AddTradeInvoiceDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.InvoiceReportDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.TradeInvoiceDTO;
import com.zeroone.star.project.j8.finance.TradeInvoiceApis;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.InvoiceReportQuery;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceKey;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 购销发票controller
 * ---------------------------------
 * 功能模块：
 * 1. 获取发票列表（支持条件查询 + 分页）
 * 2. 开票（新增发票数据）
 * 3. 导出发票数据（Excel/CSV）
 * 4. 获取购销发票报表（支持条件查询 + 分页）
 * 5. 导出报表数据（Excel/CSV）
 */
@RestController
@RequestMapping("/finance/trade-invoice")
@Api(tags = "资金管理 - 购销发票接口")
public class TradeInvoiceController implements TradeInvoiceApis {

    @Resource
    private IInvoiceService invoiceService;

    /**
     * 1.获取发票列表（支持条件查询 + 分页）
     *
     * @param tradeInvoiceQuery 查询参数
     * @return 发票列表
     */
    @GetMapping("/list/invoice")
    @ApiOperation("获取发票列表")
    public JsonVO<PageDTO<TradeInvoiceDTO>> getTradeInvoice(TradeInvoiceQuery tradeInvoiceQuery) {
        //获取发票列表逻辑
        PageDTO<TradeInvoiceDTO> pageDTO = invoiceService.listTradeInvoice(tradeInvoiceQuery);
        return JsonVO.success(pageDTO);
    }

    /**
     * 2.开票（新增发票数据）
     *
     * @param dto 发票数据
     * @return 新增发票结果
     */
    @ApiOperation(value = "开票", notes = "批量为 buy/bre/sell/sre 单据开具发票")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "参数校验失败"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping(value = "/add")
    public JsonVO<String> addTradeInvoice(@Validated @RequestBody AddTradeInvoiceDTO dto) {
        invoiceService.addTradeInvoice(dto);
        return JsonVO.success("OK");
    }

    /**
     * 3.导出的购销的发票为excel
     *
     * @param keys ID,type集合
     * @return 报表文件
     */
    @ApiOperation("导出发票数据")
    @PostMapping(value = "/export", produces = "application/octet-stream")
    public ResponseEntity<byte[]> export(@RequestBody @Valid List<TradeInvoiceKey> keys) {
        return invoiceService.exportInvoiceExcel(keys);
    }

    /*
     * 4. 获取购销发票报表（支持条件查询 + 分页）
     */
    @GetMapping("/list/report")
    @ApiOperation("获取购销发票报表(条件+分页)")
    public JsonVO<PageDTO<InvoiceReportDTO>> getInvoiceReportList(InvoiceReportQuery query) {
        PageDTO<InvoiceReportDTO> pageResult = invoiceService.getInvoiceReportList(query);
        return JsonVO.success(pageResult);
    }

    /*
     * 5.导出购销发票报表
     */
    @PostMapping("/export/report")
    @ApiOperation("导出报表数据")
    public ResponseEntity<byte[]> exportInvoiceReportExcel(@RequestBody List<String> ids) {
        return invoiceService.exportInvoiceReportExcel(ids);
    }
}
