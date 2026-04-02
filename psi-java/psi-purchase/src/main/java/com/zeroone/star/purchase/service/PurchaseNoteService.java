package com.zeroone.star.purchase.service;

import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteAddDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteBuildDTO;

import java.util.List;

public interface PurchaseNoteService {

    /**
     * 新增采购单
     * @param purchaseNoteAddDTO 采购单数据
     * @return 是否成功
     * @author Hazenix 
     */
    String addPurchaseNote(PurchaseNoteAddDTO purchaseNoteAddDTO);


    /**
     * 删除采购单（支持批量）
     * @param ids 采购单ID列表
     * @return 删除结果
     * @author Hazenix 
     */
    String deletePurchase(List<String> ids);

    /**
     * 获取生成采购退货单数据
     * @param id 采购单ID
     * @return 采购退货单数据
     * @author Hazenix 
     */
    PurchaseNoteBuildDTO getBuildBreData(String id);
}