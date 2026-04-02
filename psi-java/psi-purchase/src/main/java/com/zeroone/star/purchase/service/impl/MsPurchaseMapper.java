package com.zeroone.star.purchase.service.impl;

import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteBuyDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteInfoDTO;
import com.zeroone.star.purchase.DO.BuyDO;
import com.zeroone.star.purchase.DO.BuyInfoDO;
import com.zeroone.star.purchase.excel.BuyDetailExcel;
import com.zeroone.star.purchase.excel.BuyInfoDetailExcel;
import com.zeroone.star.purchase.excel.BuySimpleExcel;
import com.zeroone.star.purchase.excel.BuyInfoSimpleExcel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MsPurchaseMapper {

    /**
     * 采购单DO 转 采购单Excel
     * @param buyDO
     * author 小阳
     */
    BuySimpleExcel buyToBuyExcel(BuyDO buyDO);


    /**
     * 采购单DO列表 转 采购单Excel列表
     * @param buys
     * @return
     */
    List<BuySimpleExcel> buysToBuyExcels(List<BuyDO> buys);

    /**
     * 采购单DO列表 转 采购单Excel列表
     * @param buys
     * @return
     */
    List<BuyDetailExcel> buysToBuyDetailExcels(List<BuyDO> buys);

    /**
     * 采购单infoDO列表 转 采购单infoExcel列表
     * @param buyInfos
     * author 小阳
     */
    List<BuyInfoSimpleExcel> buyInfosToBuyInfoExcels(List<BuyInfoDO> buyInfos);

    /**
     * 采购单infoDO列表 转 采购单infoExcel列表
     * @param buyInfos
     * author 小阳
     */
    List<BuyInfoDetailExcel> buyInfosToBuyInfoDetailExcels(List<BuyInfoDO> buyInfos);

/*    *//**
     * 采购单buyDTO 转 采购单Excel
     * @param buyDTO
     * author 小阳
     *//*
    BuySimpleExcel buyDTOToBuyExcel(PurchaseNoteBuyDTO buyDTO);

    *//**
     * 采购单DTO列表 转 采购单Excel列表
     * @param buyDTOs
     * @return
     *//*
    List<BuySimpleExcel> buyDTOsToBuyExcels(List<PurchaseNoteBuyDTO> buyDTOs);

    *//**
     * 采购单infoDTO列表 转 采购单infoExcel列表
     * @param buyInfoDTOs
     * author 小阳
     *//*
    List<BuyInfoSimpleExcel> buyInfoDTOsToBuyInfoExcels(List<PurchaseNoteInfoDTO> buyInfoDTOs);*/
}
