package com.zeroone.star.fund.controller;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.fund.service.FundReceiptService;
import com.zeroone.star.fund.service.FundReceiptFormService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.fund.FundReceiptDetailDTO;
import com.zeroone.star.project.dto.j4.fund.FundReceiptListDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptAddDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptAuditDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptDeleteDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptUpdateDTO;
import com.zeroone.star.fund.service.FundReceiptService;
import com.zeroone.star.project.j4.FundReceiptFormApis;
import com.zeroone.star.project.query.j4.fund.FundReceiptListQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.fund.ImportResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

import javax.servlet.http.HttpServletResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 描述：资金管理-收款单控制器
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author ikun
 * @version 1.0.0
 */
@RestController
@RequestMapping("/fund/receipt")
@Api(tags = "收款单")
@Validated
public class FundReceiptFormController implements FundReceiptFormApis {

    @Autowired
    private FundReceiptService fundReceiptService;

    /**
     * 新增收款单
     * @param receiptAddDTO 收款单数据
     * @return 结果
     */


    @Autowired
    private FundReceiptFormService fundReceiptFormService;

    /**
     * 获取收款单列表（条件+分页）
     */
    @PostMapping("/fundReceiptList")
    @ApiOperation(value = "获取收款单列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<FundReceiptListDTO>> fundReceiptList(
            @ApiParam(value = "查询参数") @RequestBody FundReceiptListQuery query) {
        return fundReceiptFormService.pageList(query);
    }

    /**
     * 获取收款单详情
     */
    @PostMapping("/fundReceiptDetail")
    @ApiOperation(value = "获取收款单详情")
    @Override
    public JsonVO<FundReceiptDetailDTO> fundReceiptDetail(
           @NotNull @RequestParam("id") @ApiParam(value = "收款单ID") String id) {
        return fundReceiptFormService.selectFundReceiptDetail(id);
    }

//    public JsonVO<ImportResultVO> importSaleCollection


    /**
     * 导入资金收款单数据
     * @param file Excel文件
     * @return 导入结果
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入资金收款单数据")
    public JsonVO<ImportResultVO> importSaleCollection(
            @ApiParam(value = "Excel文件", required = true)
            @RequestPart("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return JsonVO.create(null, 9999, "请选择要导入的Excel文件");
            }

            // 检查文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return JsonVO.create(null, 9999, "请上传Excel文件(.xlsx或.xls格式)");
            }

            // 调用服务层处理导入
            ImportResultVO result = fundReceiptService.importReceiptData(file);
            return JsonVO.success(result);

        } catch (Exception e) {
            String errorMsg = "导入失败：" + (e.getMessage() != null ? e.getMessage() : "未知错误");
            ImportResultVO errorResult = new ImportResultVO();
            errorResult.setSummary(errorMsg);
            return JsonVO.fail(errorResult);
        }
    }
    @PostMapping()
    @ApiOperation("新增收款单")
    public JsonVO<String> addReceipt(@RequestBody ReceiptAddDTO receiptAddDTO) {
        if(receiptAddDTO == null){
            return JsonVO.fail("请求参数不能为空");
        }
        try {
            fundReceiptService.addReceipt(receiptAddDTO);
            return JsonVO.success("收款单创建成功");
        } catch (Exception e) {
            String errorMsg = "创建收款单失败：" + (e.getMessage() != null ? e.getMessage() : "未知错误");
            return JsonVO.fail(errorMsg);
        }
    }

    /**
     * 修改收款单
     * @param id 收款单ID
     * @param dto 收款单更新数据
     * @return 结果
     */
    @PutMapping("/{id}")
    @ApiOperation("修改收款单")
    @Override
    public JsonVO<String> updateReceipt(@PathVariable("id") String id, @RequestBody ReceiptUpdateDTO dto) {
        // 将路径中的ID设置到DTO中
        dto.setId(id);

        try {
            fundReceiptService.updateReceipt(dto);
            return JsonVO.success("收款单更新成功");
        } catch (Exception e) {
            String errorMsg = "更新收款单失败：" + (e.getMessage() != null ? e.getMessage() : "未知错误");
            return JsonVO.fail(errorMsg);
        }
    }

    /**
     * 审核收款单
     * @param receiptAuditDTO 审核数据
     * @return 结果
     */
    /**
     * 导出资金收款单简单报表
     * @return 文件字节数组响应
     */
    @GetMapping("/export/simple")
    @ApiOperation(value = "导出资金收款单简单报表")
    public ResponseEntity<byte[]> exportSimpleSaleCollection() {
        try {
            // 调用服务层生成简单报表字节数组
            byte[] excelBytes = fundReceiptService.exportSimpleReceiptReport();

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            String filename = "receipt-simple-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 导出资金收款单详细报表
     * @return 文件字节数组响应
     */
    @GetMapping("/export/detail")
    @ApiOperation(value = "导出资金收款单详细报表")
    public ResponseEntity<byte[]> exportDetailSaleCollection() {
        try {
            // 调用服务层生成详细报表字节数组
            byte[] excelBytes = fundReceiptService.exportDetailReceiptReport();

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            String filename = "receipt-detail-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PatchMapping("/audit")
    @ApiOperation("审核收款单")
    public JsonVO<String> auditReceipt(@RequestBody ReceiptAuditDTO receiptAuditDTO) {
        if (receiptAuditDTO == null) {
            return JsonVO.fail("请求参数不能为空");
        }
        try {
            fundReceiptService.auditReceipt(receiptAuditDTO);
            return JsonVO.success("审核成功");
        } catch (Exception e) {
            String errorMsg = "审核收款单失败：" + (e.getMessage() != null ? e.getMessage() : "未知错误");
            return JsonVO.fail(errorMsg);
        }
    }

    /**
     * 删除收款单
     * @param receiptDeleteDTO 删除数据
     * @return 结果
     */
    @DeleteMapping()
    @ApiOperation("删除收款单")
    public JsonVO<String> deleteReceipt(@RequestBody ReceiptDeleteDTO receiptDeleteDTO) {
        if (receiptDeleteDTO == null) {
            return JsonVO.fail("请求参数不能为空");
        }
        try {
            fundReceiptService.deleteReceipt(receiptDeleteDTO);
            return JsonVO.success("收款单删除成功");
        } catch (Exception e) {
            String errorMsg = "删除收款单失败：" + (e.getMessage() != null ? e.getMessage() : "未知错误");
            return JsonVO.fail(errorMsg);
        }
    }
}