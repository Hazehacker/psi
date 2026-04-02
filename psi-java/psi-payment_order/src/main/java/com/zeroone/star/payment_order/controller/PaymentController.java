package com.zeroone.star.payment_order.controller;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.payment_order.service.IOmyService;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.j6.payment_order.OmyImportDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j6.payment_order.OmyInfoDetailVO;
import com.zeroone.star.project.vo.j6.payment_order.OmySimpleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 付款单控制器（处理导入导出及其他业务接口）
 */
@RestController
@RequestMapping("/payment-order")
@Api(tags = "付款单管理接口")
public class PaymentController {

    @Resource
    private IOmyService omyService;

    @Resource
    private EasyExcelComponent excelComponent;
    /**
         * 批量导入付款单（json导入）
     **/
@PostMapping("/addmore")
@ApiOperation(value = "批量导入付款单（JSON格式）")
public JsonVO<String> batchImportOmyInfo(@RequestBody @Valid List<OmyImportDTO> infoList) {
    try {
        int successCount = omyService.batchImportOmyInfo(infoList);
        return JsonVO.success("导入成功，共导入" + successCount + "条数据");
    } catch (Exception e) {
        return JsonVO.fail("导入失败：" + e.getMessage());
    }
}

    /**
     * 导出简单报表（付款单+核销单）
     */
    @SneakyThrows
    @GetMapping(value = "/export/simple", produces = "application/octet-stream")
    @ApiOperation(value = "导出付款单简单报表")
    public ResponseEntity<byte[]> exportSimpleReport() {
        // 1. 查询数据
        List<OmySimpleVO> dataList = omyService.listAllForSimpleReport();

        // 2. 生成Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        excelComponent.export("付款单简单报表", out, OmySimpleVO.class, dataList);

        // 3. 响应给前端
        HttpHeaders headers = new HttpHeaders();
        String filename = "payment-simple-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ResponseEntity<byte[]> response = new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
        out.close();
        return response;
    }

    /**
     * 导出详细报表（付款单详情表）
     */
    @SneakyThrows
    @GetMapping(value = "/export/detail", produces = "application/octet-stream")
    @ApiOperation(value = "导出付款单详细报表")
    public ResponseEntity<byte[]> exportDetailReport() {
        // 1. 查询数据
        List<OmyInfoDetailVO> dataList = omyService.listAllForDetailReport();

        // 2. 生成Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        excelComponent.export("付款单详细报表", out, OmyInfoDetailVO.class, dataList);

        // 3. 响应给前端
        HttpHeaders headers = new HttpHeaders();
        String filename = "payment-detail-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ResponseEntity<byte[]> response = new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
        out.close();
        return response;
    }
}