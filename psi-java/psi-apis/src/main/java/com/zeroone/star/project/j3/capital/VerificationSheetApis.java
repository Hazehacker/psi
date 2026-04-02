package com.zeroone.star.project.j3.capital;

import com.zeroone.star.project.dto.j3.capital.*;
import com.zeroone.star.project.query.j3.capital.VerificationImportQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetVO;
import com.zeroone.star.project.vo.j3.capital.BatchOperateResultVO;
import com.zeroone.star.project.vo.j3.capital.ImportResultVO;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetAddVO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetModifyVO;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.query.j3.capital.VerificationPendingBillQuery;
import com.zeroone.star.project.query.j3.capital.VerificationBillWrittenQuery;

import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: 小夏
 * @CreateTime: 2025-10-18
 * @Description:核销单相关接口定义
 * @Version: 1.0
 */
public interface VerificationSheetApis {

    /**
     * 核销单操作（核销、反核销）
     * @param dto 操作DTO，包含type和ids
     * 批量审核核和反审核核销单
     *
     * @param dto 反审核DTO
     * @return 操作结果
     */
    JsonVO<BatchOperateResultVO> approve(@NotNull(message = "操作参数不能为空") VerificationOperationDTO dto);

    /**
     * 批量删除核销单
     *
     * @param dto 批量删除DTO
     * @return 操作结果
     */
    JsonVO<BatchOperateResultVO> deleteBatch(@NotNull(message = "删除参数不能为空") VerificationBatchDeleteDTO dto);

    /**
     * 导入核销单数据
     *
     * @param file  导入文件
     * @param query 导入查询参数
     * @return 导入结果
     */
    JsonVO<ImportResultVO> importData(@NotNull(message = "上传文件不能为空") MultipartFile file,
                                      @NotNull(message = "查询参数不能为空")
                                      @Valid VerificationImportQuery query);

    /**
     * 获取指定核销单详情
     *
     * @param id 核销单id
     * @return JsonVO<VerificationSheetDTO>
     */
    public JsonVO<VerificationSheetVO> getVerificationSheetDetail(@NotEmpty(message = "核销单ID不能为空") String id);

    /**
     * 新增核销单
     *
     * @param verificationSheetAddDTO
     * @return JsonVO<VerificationSheetAddVO>
     */
    public JsonVO<VerificationSheetAddVO> addVerificationSheet(@NotNull(message = "核销单数据不能为空") VerificationSheetAddDTO verificationSheetAddDTO,@RequestHeader("Authorization") String Authorization);

    /**
     * 修改核销单
     *
     * @param verificationSheetModifyDTO
     * @return JsonVO<VeritificationSheetAddVO>
     */
    public JsonVO<VerificationSheetModifyVO> updateVerificationSheet(@NotNull(message = "核销单修改数据不能为空") VerificationSheetModifyDTO verificationSheetModifyDTO,@RequestHeader("Authorization") String authorization);

    /**
     * 获取待核销单单据列表
     *
     * @param query 查询参数
     * @return 待核销单列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    JsonVO<PageDTO<VerificationSheetPendingBillOffDTO>> listPendingWriteOff(@NotNull(message = "查询参数不能为空") VerificationPendingBillQuery query);

    /**
     * 获取核销单列表
     *
     * @param query 查询参数
     * @return 核销单列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    JsonVO<PageDTO<VerificationSheetBillWrittenDTO>> listWrittenOff(@NotNull(message = "查询参数不能为空") VerificationBillWrittenQuery query);

    /**
     * 导出简单报表
     *
     * @param ids 核销单ID列表
     * @return ResponseEntity<byte [ ]> 文件流
     */
    public ResponseEntity<byte[]> exportBill(@NotBlank(message = "核销单ID列表不能为空") String ids);

    /**
     * 导出详细报表
     *
     * @param ids 核销单ID列表
     * @return ResponseEntity<byte [ ]> 文件流
     * @param ids 核销单ID列表（逗号分隔）
     * @return ResponseEntity<byte[]> 文件流
     */
    public ResponseEntity<byte[]> exportBillInfo(@NotBlank(message = "核销单ID列表不能为空") String ids);
}
