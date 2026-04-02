package com.zeroone.star.sale.controller;


import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.*;
import com.zeroone.star.project.j4.SaleReturnFormApis;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.sale.entity.IsSre;
import com.zeroone.star.sale.service.SaleReturnFormService;
import com.zeroone.star.project.query.j4.sale.SaleReturnQuery;
import com.zeroone.star.sale.service.SaleReturnFormService;
import com.zeroone.star.sale.service.SaleReturnFormplusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 描述：销售管理-销售单控制器类
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@RestController
@RequestMapping("/sale/return")
@Api(tags = "销售退货单")
@Validated
public class SaleReturnFormController implements SaleReturnFormApis {

    @Resource
    private SaleReturnFormService saleReturnFormService;

    @Resource
    private SaleReturnFormplusService saleReturnFormplusService;

    @ApiOperation(value = "获取退货订单")
    @GetMapping("/saleReturnList")
    @Override
    public JsonVO<PageDTO<SaleReturnDTO>> querySaleReturnList(SaleReturnQuery saleReturnQuery) {
        PageDTO<SaleReturnDTO> pageDTO = saleReturnFormService.pageSaleReturn(saleReturnQuery);
        return JsonVO.success(pageDTO);
    }
    @Override
    @PostMapping("/addSalesReturnOrder")
    @ApiOperation(value = "新增退货销售单")
    public JsonVO<String> addSalesReturnOrder(@RequestBody SalesReturnOrderDTO dto) {
        boolean returnOrder = saleReturnFormService.addSalesReturnOrder(dto);
        if(returnOrder){
            return JsonVO.success("新增成功");
        }
        else return JsonVO.fail("新增失败");
    }
    @GetMapping(value = "/exportSimpleFund",produces = "application/json")
    @SneakyThrows
    @ApiOperation("导出简单销售退货单")
    public JsonVO<String> exportSimpleFund() {
        String url = saleReturnFormService.exportDetailFund();
        return JsonVO.success(url);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取退货订单详情")
    @Override
    public JsonVO<List<SaleReturnInfoDTO>> querySaleReturnInfoList(@PathParam("id") String id) {
        List<SaleReturnInfoDTO> list =  saleReturnFormService.listSaleReturnInfo(id);
        return JsonVO.success(list);
    }

    @Override
    @PutMapping("/updateSalesReturnOrder")
    @ApiOperation(value = "修改退货销售单")
    public JsonVO<String> updateSalesReturnOrder(@RequestBody SalesReturnOrderDTO dto) {
        boolean returnOrder = saleReturnFormService.updateSalesReturnOrder(dto);
        if(returnOrder){
            return JsonVO.success("修改成功");
        }
        else return JsonVO.fail("修改失败");
    }

    @Override
    @DeleteMapping("/deleteSalesReturnOrder")
    @ApiOperation(value = "删除退货销售单")
    public JsonVO<String> deleteSalesReturnOrder(String id) {
        boolean returnOrder = saleReturnFormService.deleteSalesReturnOrder(id);
        if(returnOrder){
            return JsonVO.success("删除成功");
        }
        else return JsonVO.fail("删除失败");
    }
    @PostMapping("/importFund")
    @SneakyThrows
    @ApiOperation("导入销售退货单")
    public ResponseEntity<String> importFund(@ApiParam("导入退货单文件") MultipartFile file) {
        String url = saleReturnFormService.importFund(file);
        return ResponseEntity.ok( "导入成功");
    }

    @GetMapping(value = "/exportDetailFund",produces = "application/json")
    @SneakyThrows
    @ApiOperation("导出销售详细退货单")
    public JsonVO<String> exportDetailFund() {
        String url = saleReturnFormService.exportDetailFund();
        return JsonVO.success(url);
    }
    @PostMapping("/return/check")
    @ApiOperation(value = "核对退货单(支持批量)")
    public JsonVO<Integer> salereturncheck(@RequestBody @ApiParam(value = "退货单ID对象",required = true) SaleReturnIdsDTO saleReturnIdsDTO) {
        Integer status = saleReturnIdsDTO.getStatus();
        if (status == 1) {
            return saleReturnFormplusService.check(saleReturnIdsDTO);
        }
        return saleReturnFormplusService.uncheck(saleReturnIdsDTO);
    }
    @PostMapping("/return/examine")
    @ApiOperation(value = "审核退货单(支持批量)")
    public JsonVO<Integer> examine(@RequestBody @ApiParam(value = "退货单ID对象",required = true)SaleReturnIdsDTO saleReturnIdsDTO) {
        Integer status = saleReturnIdsDTO.getStatus();
        if(status == 1) {
            return saleReturnFormplusService.examine(saleReturnIdsDTO);
        }
        return saleReturnFormplusService.unexamine(saleReturnIdsDTO);
    }
}
