package com.zeroone.star.purchase.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.purchase.*;
import com.zeroone.star.project.j3.purchase.PurchaseOrderApis;
import com.zeroone.star.project.query.j3.purchase.PurchaseOrderQuery;
import com.zeroone.star.project.vo.JsonVO;


import com.zeroone.star.purchase.service.IBorService;

import com.zeroone.star.purchase.service.*;
import com.zeroone.star.purchase.service.IPurchaseOrderReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;

import javax.validation.constraints.NotBlank;

/**
 * @Author: 小夏
 * @CreateTime: 2025-10-18
 * @Description: 采购订单接口功能
 * @Version: 1.0
 */
@RestController
@RequestMapping("purchase-order")
@Api(tags = "采购管理-采购订单")
@Validated
@Slf4j
public class PurchaseOrderController implements PurchaseOrderApis {

    @Resource
    private IBorService borService;

    @Autowired
    private IPurchaseOrderReportService purchaseOrderReportService;

    /**
     * 新增采购订单
     * @param purchaseOrderAddDTO 采购订单数据
     * @return 新增采购订单id
     */
    @PostMapping("add")
    @ApiOperation("新增采购订单")
    public JsonVO<String> addPurchaseOrder(@Validated @RequestBody PurchaseOrderAddDTO purchaseOrderAddDTO) {
        return borService.addPurchaseOrder(purchaseOrderAddDTO);
    }

    /**
     * 修改采购订单
     * @param purchaseOrderUpdateDTO 采购订单数据
     * @return 修改结果
     */
    @ApiOperation("修改采购订单")
    @PutMapping("update")
    public JsonVO<String> updatePurchaseOrder(@Validated @RequestBody PurchaseOrderUpdateDTO purchaseOrderUpdateDTO) {
        return borService.updatePurchaseOrder(purchaseOrderUpdateDTO);
    }

    /**
     * 删除采购订单
     * @param ids 采购订单ID
     * @return 删除结果
     */
    @ApiOperation("删除采购订单(支持批量)")
    @DeleteMapping("delete")
    @Override
    public JsonVO<String> deletePurchaseOrder(@RequestBody List<String> ids) {
        return borService.deletePurchaseOrder(ids);
    }

    /**
     * 导出详细报表
     * @param ids 要导出的采购订单id列表
     * @return 包含Excel文件Base64字符串的JsonVO
     */
    @ApiOperation("导出详细报表")
    @PostMapping("/export/detail")
    @Override
    public JsonVO<String> exportPurchaseOrderDetailReport(@RequestBody List<String> ids) {
        try{
            // 调用服务层获取Excel文件的字节数组
            byte [] data = purchaseOrderReportService.exportPurchaseOrderReport(ids);

            // 将byte[] 编码为Base64字符串
            String base64Data = Base64.getEncoder().encodeToString(data);

            // 返回包含Base64字符串的JsonVO
            // 客户端解码Base64并触发文件下载
            return JsonVO.success(base64Data);
        } catch (Exception e){
            log.error("导出采购订单报表失败:", e);
            return JsonVO.fail("导出Excel失败" + e.getMessage());
        }
    }


    @Resource
    private IBuyService buyService;

    /**
     * 获取采购订单列表(条件+分页)
     * @Author 待兼唐怀瑟
     * @param query
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("获取采购订单列表(条件+分页)")
    @Override
    public JsonVO<PageDTO<PurchaseOrderListDTO>> listPurchaseOrders(@Validated PurchaseOrderQuery query) {
        return JsonVO.success(borService.listPurchaseOrders(query));
    }

    /**
     * 获取指定采购订单详情
     * @Author 待兼唐怀瑟
     * @param id
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation("获取指定采购订单详情")
    @Override
    public JsonVO<PurchaseOrderDetailDTO> getDetailPurchaseOrder(@RequestParam String id) {
        return JsonVO.success(borService.getPurchaseOrderDetail(id));
    }

    /**
     * 获取生成采购单数据
     * @Author 待兼唐怀瑟
     * @param id
     * @return
     */
    @GetMapping("/generate-purchaseData")
    @ApiOperation("获取生成采购单数据")
    @Override
    public JsonVO<PurchaseNoteGenerateDataDTO> generatePurchaseData(@RequestParam @Validated String id) {
        return JsonVO.success(buyService.generatePurchaseData(id));
    }

    /**
     * 审核/反审核(支持批量)
     * @Author 待兼唐怀瑟
     * @param dto
     * @return
     */
    @PutMapping("/audit")
    @ApiOperation("审核/反审核(支持批量)")
    @Override
    public JsonVO<List<String>> batchApprovePurchaseOrders(@RequestBody @Validated AuditRequestDTO dto) {
        return JsonVO.success(borService.batchApprovePurchaseOrders(dto));
    }

    @PostMapping("/import-bor")
    @ApiOperation("导入数据")
    @Override
    public JsonVO<String> importBor(@RequestParam("file") MultipartFile file) {
        if (borService.importBor(file)) {
            return JsonVO.success("导入数据成功!");
        }else {
            return JsonVO.fail("导入数据失败!");
        }
    }

    @GetMapping("/export-bor")
    @ApiOperation("导出简单报表")
    @Override
    public ResponseEntity<byte[]> exportBor(@RequestParam("pid") @NotBlank(message = "采购订单ID列表不能为空") String pid) {
        // 拆分参数，过滤空值和去空格
        List<String> ids = Arrays.stream(pid.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        return borService.exportBor(ids);
    }
}
