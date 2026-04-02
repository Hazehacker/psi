package com.zeroone.star.purchase.service;


import com.zeroone.star.project.query.j3.capital.PurchaseNoteQuery;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteGenerateDataDTO;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteListDTO;

import com.zeroone.star.purchase.DO.BuyDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.zeroone.star.project.dto.j3.purchase.PurchaseAuditDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseCheckDTO;
import com.zeroone.star.project.dto.j3.purchase.RevisePurchaseDTO;
import com.zeroone.star.project.vo.JsonVO;

import java.util.List;

/**
 * <p>
 * 采购单 服务类
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
public interface IBuyService extends IService<BuyDO> {



    void exportPurchaseNoteStreaming(List<String> ids, ByteArrayOutputStream outputStream);

    void exportPurchaseNoteDetailStreaming(List<String> ids, ByteArrayOutputStream baos);

    JsonVO<String> importPurchaseNote(MultipartFile excelFile) throws IOException;
    /**
     * @Description: 修改采购单
     * @Author: 正念
     */
    JsonVO<String> updateBuy(RevisePurchaseDTO revisePurchaseDTO);

    /**
     * 核对反核对（批量）
     * @param checkList
     * @return
     */
    JsonVO<String> checkStatus(PurchaseCheckDTO checkList);

    /**
     * 审核反审核（批量）
     * @param auditList
     * @return
     */
    JsonVO<String> examineStatus(PurchaseAuditDTO auditList);

    /**
     * 生成采购单数据
     */
    PurchaseNoteGenerateDataDTO generatePurchaseData(String id);

    /**
     * 根据条件分页查询采购单列表
     * @param query 查询条件
     * @return 采购单列表分页数据
     * @author Hazenix 
     */
    PageDTO<PurchaseNoteListDTO> listPurchaseNote(PurchaseNoteQuery query);
}
