package com.zeroone.star.sale.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderImportDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderInfoDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderListDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderVerifyDTO;
import com.zeroone.star.project.dto.j4.sale.*;
import com.zeroone.star.project.j4.SaleOrderApis;
import com.zeroone.star.project.query.j4.sale.SaleOrderQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.sale.service.SaleOrderInfoService;
import com.zeroone.star.sale.entity.SaleOrderImportResult;
import com.zeroone.star.sale.service.SaleOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 描述：销售管理-销售订单控制器类
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@RestController
@RequestMapping("/sale/order")
@Api(tags = "销售订单")
@Validated
public class SaleOrderController implements SaleOrderApis {
    @Resource
    private SaleOrderService saleOrderService;

    @Resource
    private SaleOrderInfoService saleOrderInfoService;

    /**
     * 获取销售订单列表（条件+分页）
     */
    @PostMapping("/saleOrdersList")
    @ApiOperation(value = "获取销售订单列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<SaleOrderListDTO>> saleOrdersList(
            @ApiParam(value = "查询参数") @RequestBody SaleOrderQuery query) {
        return saleOrderService.page(query);
    }

    /**
     * 获取指定销售订单详情
     */
    @GetMapping("/saleOrderInfo")
    @ApiOperation(value = "获取指定销售订单详情")
    @Override
    public JsonVO<List<List<SaleOrderInfoDTO>>> saleOrderDetail(
            @ApiParam(value = "客户")String customer,
            @ApiParam(value = "单据日期") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time,
            @ApiParam(value = "单据编号")String number) {
        return saleOrderInfoService.getByQuery(customer, time, number);
    }

    /**
     * 获取生成销售单数据
     */
    @GetMapping("/getGenerateSaleOrderData")
    @ApiOperation(value = "获取生成销售单数据")
    @Override
    public JsonVO<SaleOrderGenerateDTO> getGenerateSaleOrderData(@ApiParam(value = "销售单ID", required = true) String saleId) {
       return saleOrderService.generateSaleOrderData(saleId);
    }

    /**
     * 获取生成采购订单数据
     */
    @GetMapping("/getGeneratePurchaseOrderData")
    @ApiOperation(value = "获取生成采购订单数据")
    @Override
    public JsonVO<PurchaseOrderGenerateDTO> getGeneratePurchaseOrderData(@ApiParam(value = "采购单ID", required = true) String purchaseId) {
        return saleOrderService.generatePurchaseOrderData(purchaseId);
    }

    /**
     * 批量删除销售订单
     */
    @DeleteMapping("/saleOrdersDelete")
    @ApiOperation(value = "批量删除销售订单")
    @Override
    public JsonVO<Boolean> saleOrderDelete(
            @ApiParam(value = "订单ID列表", required = true, example = "[SO1001, SO1002]")
            @RequestBody List<String> ids) {

        if(saleOrderService.saleOrderDelete(ids)){
            return JsonVO.success(true);
        }
        return JsonVO.fail(false);
    }

    /**
     * 新增销售订单
     */
    @PostMapping("/saleOrdersAdd")
    @ApiOperation(value = "新增销售订单")
    @Override
    public JsonVO<Boolean> saleOrderAdd(
            @ApiParam(value = "销售订单实体") @RequestBody SaleOrderListDTO dto) {
        if(saleOrderService.saleOrderAdd(dto)){
            return JsonVO.success(true);
        }
        return JsonVO.fail(false);
    }

    /**
     * 修改销售订单
     */
    @PutMapping("/saleOrdersUpdate")
    @ApiOperation(value = "修改销售订单")
    @Override
    public JsonVO<Boolean> saleOrderChange(
            @ApiParam(value = "销售订单实体") @RequestBody SaleOrderListDTO dto) {
        if(saleOrderService.saleOrderChange(dto)){
            return JsonVO.success(true);
        }
        return JsonVO.fail(false);
    }

    /**
     * 批量审核/反审核销售订单
     */
    @PostMapping("/saleOrdersVerify")
    @ApiOperation(value = "批量审核/反审核销售订单", notes = "根据传入的订单ID列表批量审核或反审核销售订单。num=1 表示审核，num=0 表示反审核。")
    @Override
    public JsonVO<Boolean> verifyOrder(
            @ApiParam(value = "审核请求对象", required = true)
            @RequestBody SaleOrderVerifyDTO request) {

        if(saleOrderService.verifyOrder(request)){
            return JsonVO.success(true);
        }
        return JsonVO.fail(false);
    }

    /**
     * 上传销售订单CSV文件并导入数据
     */
    @PostMapping("/saleOrderImport")
    @ApiOperation(value="导入数据")
    @Override
    public JsonVO<Boolean> saleOrderImport(
            @ApiParam(value = "销售订单excel文件", required = true)
            @RequestPart("file") MultipartFile file) {
        SaleOrderImportResult result= saleOrderService.importFromExcel(file);
        return JsonVO.success(result.getFailCount()==0);
    }



    /**
     * 导出简单报表（通过单据编号筛选）
     */
    @GetMapping("/saleOrderExportSimple")
    @ApiOperation(value = "导出简单报表", notes = "根据单据编号导出对应的简单报表")
    @Override
    public ResponseEntity<byte[]> saleOrderExportSimple(
            @ApiParam(value = "单据编号（必填）", required = true)
            @RequestParam("billNo") List<String> billNo) {
        byte[] csvData = saleOrderService.exportSimple(billNo);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"simple_report.csv\"")
                .body(csvData);
    }

    /**
     * 导出详细报表（通过单据编号筛选）
     */
    @GetMapping("/saleOrderExportDetail")  // 补充路径前缀 /，保持规范
    @ApiOperation(value = "导出详细报表", notes = "根据单据编号导出对应的详细报表")
    @Override
    public ResponseEntity<byte[]> saleOrderExportDetail(
            @ApiParam(value = "单据编号（必填）", required = true)
            @RequestParam("billNo") List<String> billNo) {
        byte[] csvData = saleOrderService.exportDetail(billNo);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"detail_report.csv\"")
                .body(csvData);
    }






}





