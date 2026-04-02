package com.zeroone.star.capital.controller;

import com.zeroone.star.capital.service.IBillInfoService;

import com.zeroone.star.capital.service.IBillService;
import com.zeroone.star.project.dto.j3.capital.*;
import com.zeroone.star.project.j3.capital.VerificationSheetApis;
import com.zeroone.star.project.query.j3.capital.VerificationBillWrittenQuery;
import com.zeroone.star.project.query.j3.capital.VerificationPendingBillQuery;
import com.zeroone.star.project.query.j3.capital.VerificationImportQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetVO;
import com.zeroone.star.project.vo.j3.capital.BatchOperateResultVO;
import com.zeroone.star.project.vo.j3.capital.ImportResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetAddVO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetModifyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.zeroone.star.project.dto.PageDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 小夏
 * @CreateTime: 2025-10-18
 * @Description: 核销单接口功能
 * @Version: 1.0
 */
@RestController
@RequestMapping("/verification-sheet")
@Api(tags = "资金管理-核销单")
@Slf4j
@Validated
@SuppressWarnings("all")
public class VerificationSheetController implements VerificationSheetApis {

    @Autowired
    private IBillService billService;

    @Resource
    IBillInfoService billInfoService;


    /**
     * 核销单操作（核销、反核销）
     *
     * @param dto 操作参数，包含type（VERIFY-核销, UNVERIFY-反核销）和ids
    @Resource
    */
    /**
     * 批量审核与反审核核销单
     * @param dto 审核参数

     * @return 批量操作结果
     * @author: 俊杰
     * @date: 2025/10/25
     */
    @ApiOperation(value = "核销单操作（核销、反核销）")
    @PostMapping("approve")
    @Override
    public JsonVO<BatchOperateResultVO> approve(@Validated @RequestBody VerificationOperationDTO dto) {
        try {
            BatchOperateResultVO result = billService.approve(dto);
            return JsonVO.success(result);
        } catch (Exception e) {
            log.error("核销/反核销操作失败", e);
            // 返回空结果对象，并在message中包含错误信息
            BatchOperateResultVO emptyResult = BatchOperateResultVO.builder()
                    .successCount(0)
                    .failCount(0)
                    .totalCount(0)
                    .build();
            return JsonVO.create(emptyResult, 500, e.getMessage());
        }
    }

    /**
     * 批量删除核销单
     *
     * @param dto 批量删除参数
     * @return 批量操作结果
     * @author: 俊杰
     * @date: 2025/10/25
     */
    @ApiOperation(value = "批量删除核销单")
    @DeleteMapping("delete-batch")
    @Override
    public JsonVO<BatchOperateResultVO> deleteBatch(@Validated @RequestBody VerificationBatchDeleteDTO dto) {
        try {
            BatchOperateResultVO result = billService.batchDelete(dto);
            return JsonVO.success(result);
        } catch (Exception e) {
            log.error("批量删除核销单失败", e);
            // 返回空结果对象，并在message中包含错误信息
            BatchOperateResultVO emptyResult = BatchOperateResultVO.builder()
                    .successCount(0)
                    .failCount(0)
                    .totalCount(0)
                    .build();
            return JsonVO.create(emptyResult, 500, e.getMessage());
        }
    }

    /**
     * 导入核销单数据
     *
     * @param file 上传的Excel文件
     * @param query 导入查询参数
     * @return 导入结果
     * @author: 俊杰
     * @date: 2025/10/25
     */
    @ApiOperation(value = "导入核销单数据")
    @PostMapping("import")
    @Override
    public JsonVO<ImportResultVO> importData(
            @RequestParam("file") MultipartFile file,
            VerificationImportQuery query) {
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                ImportResultVO emptyResult = ImportResultVO.builder()
                        .successCount(0)
                        .failCount(0)
                        .totalCount(0)
                        .build();
                return JsonVO.create(emptyResult, 400, "上传文件不能为空");
            }
            
            // 验证文件类型
            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
                ImportResultVO emptyResult = ImportResultVO.builder()
                        .successCount(0)
                        .failCount(0)
                        .totalCount(0)
                        .build();
                return JsonVO.create(emptyResult, 400, "只支持Excel文件格式(.xlsx或.xls)");
            }
            
            ImportResultVO result = billService.importData(file, query);
            return JsonVO.success(result);
        } catch (Exception e) {
            log.error("导入核销单数据失败", e);
            // 返回空结果对象，并在message中包含错误信息
            ImportResultVO emptyResult = ImportResultVO.builder()
                    .successCount(0)
                    .failCount(0)
                    .totalCount(0)
                    .build();
            return JsonVO.create(emptyResult, 500, e.getMessage());
        }
    }

    /**
     * 获取指定核销单详情
     * @param id 核销单id
     * @return JsonVO<VerificationSheetDTO>
     * @author: a
     * @date: 2025/10/28
     */
    @PostMapping("/bill/get")
    @ApiOperation(value = "获取指定核销单详情")
    @Override
    public JsonVO<VerificationSheetVO> getVerificationSheetDetail(@RequestParam("id") String id) {
        return JsonVO.success(billService.getVSDetailById(id));
    }

    /**
     * 新增核销单
     * @param verificationSheetAddDTO 添加核销单数据
     * @return JsonVO<VerificationSheetAddVO>
     * @author: a
     * @date: 2025/10/28
     */
    @PostMapping("/bill/save")
    @ApiOperation(value = "新增核销单")
    @Override
    public JsonVO<VerificationSheetAddVO> addVerificationSheet(@Validated @RequestBody VerificationSheetAddDTO verificationSheetAddDTO, @RequestHeader("Authorization") String Authorization) {
        return JsonVO.success(billService.addVS(verificationSheetAddDTO, Authorization));
    }

    /**
     * 修改核销单
     * @param verificationSheetModifyDTO 修改核销单数据
     * @return JsonVO<VerificationSheetModifyVO>
     * @author: a
     * @date: 2025/10/28
     */
    @PostMapping("/bill/update")
    @ApiOperation(value = "修改核销单")
    @Override
    public JsonVO<VerificationSheetModifyVO> updateVerificationSheet(@Validated @RequestBody VerificationSheetModifyDTO verificationSheetModifyDTO, @RequestHeader("Authorization") String authorization) {
        return JsonVO.success(billService.updateVS(verificationSheetModifyDTO, authorization));
    }
    /**
     * 获取需要核销的单据列表(条件+分页)
     *
     * @param query 查询参数
     * @return 需要核销的单据列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    @Override
    @GetMapping("/list/pending-bills")
    @ApiOperation("获取需要核销的单据列表(条件+分页)")
    public JsonVO<PageDTO<VerificationSheetPendingBillOffDTO>> listPendingWriteOff(@ModelAttribute VerificationPendingBillQuery query) {

        log.info("查询核销单参数：{}", query);
        return billService.listPendingWriteOff(query);
    }

    @GetMapping("/export-billInfo")
    @ApiOperation("导出详细报表")
    @Override
    public ResponseEntity<byte[]> exportBillInfo(@RequestParam("ids") @NotBlank(message = "核销单ID列表不能为空") String ids) {
        // 拆分参数，过滤空值和去空格
        List<String> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        return billInfoService.exportBillInfo(idList);
    }

    /**
     * 获取核销单列表(条件+分页)
     *
     * @param query 查询参数
     * @return 核销单列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    @Override
    @GetMapping("/list/bill-written")
    @ApiOperation("获取核销单列表(条件+分页)")
    public JsonVO<PageDTO<VerificationSheetBillWrittenDTO>> listWrittenOff(@ModelAttribute VerificationBillWrittenQuery query) {
        return billService.listWrittenOff(query);
    }

    @GetMapping("/export-bill")
    @ApiOperation("导出简单报表")
    @Override
    public ResponseEntity<byte[]> exportBill(@RequestParam("ids") @NotBlank(message = "核销单ID列表不能为空") String ids) {
        // 拆分参数，过滤空值和去空格
        List<String> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        return billService.exportBill(idList);
    }

}
