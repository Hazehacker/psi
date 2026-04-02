package com.zeroone.star.project.j3.purchase;

import com.zeroone.star.project.dto.j3.purchase.*;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.query.j3.purchase.PurchaseOrderQuery;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author:小夏
 * @CreateTime: 2025-10-18
 * @Description:采购订单相关接口定义
 * @Version: 1.0
 */
public interface PurchaseOrderApis {

    /**
     * 新增采购订单
     * @param purchaseOrderAddDTO 采购订单数据
     * @return 新增结果
     */
    JsonVO<String> addPurchaseOrder(@NotNull(message = "采购订单数据不能为空") PurchaseOrderAddDTO purchaseOrderAddDTO);

    /**
     * 修改采购订单
     * @param purchaseOrderUpdateDTO 采购订单数据
     * @return 修改结果
     */
    JsonVO<String> updatePurchaseOrder(@NotNull(message = "采购订单数据不能为空") PurchaseOrderUpdateDTO purchaseOrderUpdateDTO);

    /**
     * 删除采购订单
     * @param ids 采购订单id
     * @return 删除结果
     */
    JsonVO<String> deletePurchaseOrder(@NotEmpty(message = "ID列表不能为空") List<String> ids);

    /**
     * 导出采购订单详细报表
     * @param ids 要导出的采购订单ID列表
     * @return 包含Excel文件Base64字符串的JsonVO
     * @author: 加减法
     */
    JsonVO<String> exportPurchaseOrderDetailReport(@NotEmpty(message = "ID列表不能为空") List<String> ids);

    /**
     * 查询采购订单列表
     * @param query
     * @return
     */
    JsonVO<PageDTO<PurchaseOrderListDTO>> listPurchaseOrders(@NotNull(message = "查询条件不能为空") PurchaseOrderQuery query);

    /**
     * 获取指定订单详情
     * @param id
     * @return
     */
    JsonVO<PurchaseOrderDetailDTO> getDetailPurchaseOrder(@NotNull(message = "采购订单ID不能为空") String id);

    /**
     * 获取生成采购单数据
     * @param id
     * @return
     */
    JsonVO<PurchaseNoteGenerateDataDTO> generatePurchaseData(@NotNull(message = "采购单生成数据不能为空") String id);

    /**
     * 审核/反审核(支持批量)
     * @param dto
     * @return
     */
    JsonVO<List<String>> batchApprovePurchaseOrders(@NotNull(message = "审核ID列表不能为空") AuditRequestDTO dto);

    /**
     * 导入数据
     * @param file
     * @return
     */
    JsonVO<String> importBor(@NotNull(message = "文件不能为空") MultipartFile file);

    /**
     * 导出简单报表
     * @param pid 采购订单ID列表（逗号分隔）
     * @return
     */
    ResponseEntity<byte[]> exportBor(@NotBlank(message = "采购订单ID列表不能为空") String pid);
}
