package com.zeroone.star.purchase.service;


import com.zeroone.star.project.dto.j3.purchase.PurchaseOrderAddDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseOrderUpdateDTO;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.purchase.AuditRequestDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteGenerateDataDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseOrderDetailDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseOrderListDTO;
import com.zeroone.star.project.query.j3.purchase.PurchaseOrderQuery;

import com.zeroone.star.purchase.DO.BorDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import java.util.List;

/**
 * <p>
 * 采购订单 服务类
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
public interface IBorService extends IService<BorDO> {
    /**
     * 获取采购订单列表(条件+分页)
     */
    PageDTO<PurchaseOrderListDTO> listPurchaseOrders(PurchaseOrderQuery query);

    /**
     * 获取采购订单详情
     */
    PurchaseOrderDetailDTO getPurchaseOrderDetail(String id);

    /**
     * 新增采购订单
     * @param purchaseOrderAddDTO
     * @return
     */
    JsonVO<String> addPurchaseOrder(PurchaseOrderAddDTO purchaseOrderAddDTO);

    /**
     * 修改采购订单
     * @param purchaseOrderUpdateDTO
     * @return
     */
    JsonVO<String> updatePurchaseOrder(PurchaseOrderUpdateDTO purchaseOrderUpdateDTO);

    /**
     * 删除采购订单
     * @param ids
     * @return
     */
    JsonVO<String> deletePurchaseOrder(List<String> ids);
    /**
     * 批量审核/反审核
     */
    List<String> batchApprovePurchaseOrders(AuditRequestDTO dto);

    /**
     * 导出简单报表
     * @author Hazenix 
     * @param ids
     * @return
     */
    ResponseEntity<byte[]> exportBor(List<String> ids);

    /**
     * 导入数据
     * @author Hazenix 
     * @param file
     */
    Boolean importBor(MultipartFile file);

}
