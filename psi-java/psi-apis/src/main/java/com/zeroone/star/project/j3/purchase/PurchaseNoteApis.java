package com.zeroone.star.project.j3.purchase;

import com.zeroone.star.project.dto.PageDTO;

import com.zeroone.star.project.dto.j3.purchase.*;

import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteBuildDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteAddDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteInfoDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteListDTO;
import com.zeroone.star.project.query.j3.capital.PurchaseNoteQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @Author: 小夏
 * @CreateTime: 2025-10-18
 * @Description:采购单相关接口定义
 * @Version: 1.0
 */
public interface PurchaseNoteApis {

    /**
     * 获取采购单列表（条件＋分页）
     * @param query 查询参数
     * @return 采购单列表
     * @author: 加减法
     */
    JsonVO<PageDTO<PurchaseNoteListDTO>> listPurchaseNote(@NotNull(message = "查询参数不能为空") PurchaseNoteQuery query);

    /**
     * 获取指定采购单详情
     * @param id 采购单id
     * @return 采购单详情
     * @author: 加减法
     */
    JsonVO<PurchaseNoteInfoDTO> getPurchaseNoteInfo(@ApiParam(value = "采购单的主键ID", required = true, example = "1")
                                                    @NotEmpty(message = "ID不能为空")
                                                    String id);

    /**
     * 导入采购单
     * @author: 小阳
     * @param excelFile excel文件
     * @return
     */
    JsonVO<String> importPurchaseNote(
            @NotNull(message = "文件不能为空")
            MultipartFile excelFile
    );

    /**
     * 导出采购单简易报表
     * @author: 小阳
     * @param ids 采购单id
     */
    ResponseEntity<byte[]> exportPurchaseNoteSimpleReport(
            @NotEmpty(message = "采购单ID列表不能为空")
            List< String> ids
    );

    /**
     * 导出采购单详细报表
     * @author: 小阳
     * @param ids
     * @return
     */
    ResponseEntity<byte[]> exportPurchaseNoteDetailReport(
            @NotEmpty(message = "采购单ID列表不能为空")
            List< String> ids
    );

    /**
     * 获取生成采购退货单数据
     *
     * @param buyId 采购单id
     * @return BreDTO 返回生成采购退货单数据
     */
    JsonVO<PurchaseNoteBuildDTO> getBuildBre(@NotEmpty(message = "ID不能为空") String buyId);

    /**
     *新增采购单
     *
     * @param purchaseNoteAddDTO 采购单参数
     * @return String
     */
    JsonVO<String> addPurchase(@RequestBody @NotNull(message = "采购单参数不能为空") PurchaseNoteAddDTO purchaseNoteAddDTO);

    /**
     * 删除采购单（支持批量）
     *
     * @param ids 采购单id
     * @return Strign
     */
    JsonVO<String> deletePurchase(@RequestBody @NotEmpty(message = "采购单ID列表不能为空") List<String> ids);

    /***
     * 修改采购单
     * @return 成功或者失败
     */
    JsonVO<String> revisePurchase(@NotNull(message = "采购单修改参数不能为空") @Valid RevisePurchaseDTO revisePurchaseDTO);

    /**
     * 核对反核对（可以批量）
     * @return 成功或者失败
     */
    JsonVO<String> verification(@RequestBody  PurchaseCheckDTO checkList);

    /**
     * 审核反审核（可以批量）
     * @return 成功或者失败
     */
    JsonVO<String> audit(@RequestBody  PurchaseAuditDTO auditList);
}
