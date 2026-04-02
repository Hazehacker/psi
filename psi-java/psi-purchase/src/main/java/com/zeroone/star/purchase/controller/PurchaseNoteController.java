package com.zeroone.star.purchase.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.purchase.*;
import com.zeroone.star.project.j3.purchase.PurchaseNoteApis;
import com.zeroone.star.project.query.j3.capital.PurchaseNoteQuery;
import com.zeroone.star.project.vo.JsonVO;



import com.zeroone.star.purchase.service.PurchaseNoteService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.zeroone.star.purchase.service.IBuyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import com.zeroone.star.purchase.service.IBuyInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.zeroone.star.project.dto.j3.purchase.RevisePurchaseDTO;

import javax.annotation.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * @Author: 小夏
 * @CreateTime: 2025-10-18
 * @Description: 采购单接口功能
 * @Version: 1.0
 */
@RestController
@RequestMapping("purchase-note")
@Api(tags = "采购管理-采购单")
@Validated
@Slf4j
public class PurchaseNoteController implements PurchaseNoteApis {
    @Autowired
    private IBuyService iBuyService;


    @Autowired
    private PurchaseNoteService purchaseNoteService;

    @Resource
    private IBuyService buyService;
    @Autowired
    private IBuyInfoService buyInfoService;
    @ApiOperation("获取采购单列表(条件＋分页)")
    @GetMapping("/list")
    public JsonVO<PageDTO<PurchaseNoteListDTO>> listPurchaseNote(PurchaseNoteQuery query) {
        PageDTO<PurchaseNoteListDTO> result = buyService.listPurchaseNote(query);
        return JsonVO.success(result);
    }

    @ApiOperation("获取指定采购单详情")
    @GetMapping("/info")
    public JsonVO<PurchaseNoteInfoDTO> getPurchaseNoteInfo(@RequestParam("id") String id) {
        PurchaseNoteInfoDTO purchaseNoteInfo = buyInfoService.getPurchaseNoteInfo(id);
        if (purchaseNoteInfo == null) {
            return JsonVO.fail((PurchaseNoteInfoDTO) null);
        }
        return JsonVO.success(purchaseNoteInfo);
    }

    /**
     * 导入数据
     * author: Hazenix 
     */
    @ApiOperation("导入数据")
    @PostMapping("/import")
    public JsonVO<String> importPurchaseNote(MultipartFile excelFile) {
        log.info("开始导入采购单数据");
        // 校验是否是excel文件
        // 是
        try {
            return buyService.importPurchaseNote(excelFile);
        } catch (IOException e) {
            log.error("导入采购单数据失败", e);
            return JsonVO.fail("导入采购单数据失败");
        }
    }

    /**
     * 导出简易报表
     * author: Hazenix 
     */
    @ApiOperation("导出简易报表")
    @GetMapping(value = "/export/simple", produces = "application/octet-stream")
    public ResponseEntity<byte[]> exportPurchaseNoteSimpleReport(
            @RequestParam("ids") List< String> ids) {
        log.info("开始导出采购单simple报表,ids = {}",ids);

        // 在括号里面创造输出流，jvm会自动关闭流
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            // 1. 调用service把数据写到输出流中
            buyService.exportPurchaseNoteStreaming(ids, baos);
            byte[] bytes = baos.toByteArray();
            // 2.生成唯一文件名（时间戳或 UUID）
            String filename = "采购订单简易报表-" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    + ".xlsx";

            // 3.处理中文文件名兼容性,
            // 指定处理方式
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(filename, StandardCharsets.UTF_8)
                    .build();

            // 响应头
            HttpHeaders headers = new HttpHeaders();

            headers.setContentDisposition(contentDisposition);

            // 1. 设置响应类型为excel
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentLength(bytes.length);

            // 指定编码
            headers.set("Content-Encoding", "UTF-8");

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("导出采购单simple报表失败", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 导出详细报表
     * author: Hazenix 
     */
    @ApiOperation("导出详细报表")
    @GetMapping("/export/detail")
    public ResponseEntity<byte[]> exportPurchaseNoteDetailReport(@RequestParam("ids")List<String> ids) {
        log.info("开始导出采购单detail报表,ids = {}",ids);
        // TODO 参数校验，只能传数字。
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            // 1. 调用service把数据写到输出流中
            buyService.exportPurchaseNoteDetailStreaming(ids, baos);

            // 2.生成唯一文件名（时间戳或 UUID）
            String filename = "采购单-" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    + ".zip";

            // 3.处理中文文件名兼容性
            String encodedFileName = URLEncoder.encode(filename, "UTF-8")
                    .replaceAll("\\+", "%20");

            // 4.设置响应头
            HttpHeaders headers = new HttpHeaders();
            // 更改编码格式，防止乱码。
            // 并指定处理方式
            headers.add("Content-Disposition",
                    "attachment; filename*=UTF-8''" + encodedFileName);
            // 设置响应类型
            headers.setContentType(MediaType.parseMediaType("application/zip"));
            // 指定数据内容编码
            headers.set("Content-Encoding", "UTF-8");

            // 5.返回响应数据
            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("导出采购单simple报表失败", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * 获取生成采购退货单数据
     * buildBre 生成采购退单 传入一个ID, 返回一个采购退单详细信息
     *
     * @param
     * @return BreDTO 返回生成采购退货单数据
     * @author Hazenix 
     */
    @Override
    @ApiOperation("获取生成采购退货单数据")
    @GetMapping("/getBuildBre")
    public JsonVO<PurchaseNoteBuildDTO> getBuildBre( @ApiParam(value = "采购单ID", required = true, example = "PUR20251103001") String id) {
        try {
            PurchaseNoteBuildDTO result = purchaseNoteService.getBuildBreData(id);
            return JsonVO.success(result);
        } catch (Exception e) {
            return JsonVO.create(null, 9999,"获取生成采购退货单数据失败"+e.getMessage());
        }
    }

    /***
     * 修改采购单
     * @return 采购单表
     * @author Hazenix 
     */
    @PostMapping("/revise")
    @ApiOperation(value = "修改采购单")
    @Override
    public JsonVO<String> revisePurchase(@RequestBody RevisePurchaseDTO revisePurchaseDTO) {
        return iBuyService.updateBuy(revisePurchaseDTO);
    }

    /**
     * 新增采购单
     *
     * @param purchaseNoteAddDTO 采购单参数
     * @return String
     * @author Hazenix 
     */
    @Override
    @ApiOperation("新增采购单")
    @PostMapping("/add")
    public JsonVO<String> addPurchase(@RequestBody @Validated PurchaseNoteAddDTO purchaseNoteAddDTO){
        String result = purchaseNoteService.addPurchaseNote(purchaseNoteAddDTO);
        if (!result.equals("新增采购单失败")) {
            return JsonVO.success(result);
        } else {
            return JsonVO.fail("新增采购单失败");
        }
    }

    /**
     * 删除采购单（支持批量）
     *
     * @param ids 采购单id
     * @return String
     * @author Hazenix 
     */
    @Override
    @ApiOperation(value = "删除采购单", notes = "批量删除采购单，传入采购单ID列表")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "ids",
                    value = "采购单ID列表",
                    required = true,
                    dataType = "java.util.List",
                    paramType = "body", // 关键：指定为请求体参数
                    examples = @io.swagger.annotations.Example(value = {
                            @io.swagger.annotations.ExampleProperty(
                                    mediaType = "application/json",
                                    value = "[\"PUR20251103001\", \"PUR20251103002\", \"PUR20251103003\"]" // 请求体示例（字符串数组）
                            )
                    })
            )
    })
    @PostMapping("/delete")
    public JsonVO<String> deletePurchase(@RequestBody List<String> ids) {
        try {
            String result = purchaseNoteService.deletePurchase(ids);
            return JsonVO.success(result);
        } catch (Exception e) {
            return JsonVO.fail(e.getMessage());
        }
    }

    /**
     * 核对反核对（可以批量）
     * @return 核销详情
     */
    @PostMapping("/verification")
    @ApiOperation(value = "核对/反核对(支持批量)")
    @Override
    public JsonVO<String> verification(@RequestBody PurchaseCheckDTO checkList) {
        return iBuyService.checkStatus(checkList);
    }

    /**
     * 审核反审核（可以批量）
     * @return 审核详情
     */
    @PostMapping("/audit")
    @ApiOperation(value = "审核/反审核(支持批量)")
    @Override
    public JsonVO<String> audit(@RequestBody PurchaseAuditDTO auditList) {
        return iBuyService.examineStatus(auditList);
    }
}
