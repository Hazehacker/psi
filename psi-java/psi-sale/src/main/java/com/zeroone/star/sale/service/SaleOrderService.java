package com.zeroone.star.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.*;

import com.zeroone.star.project.dto.j4.sale.PurchaseOrderGenerateDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderGenerateDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderListDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderVerifyDTO;
import com.zeroone.star.project.query.j4.sale.SaleOrderQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import java.util.List;
import com.zeroone.star.sale.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 销售订单服务接口
 * 包含订单分页查询、CSV导入、报表导出等核心业务能力
 */
public interface SaleOrderService extends IService<SaleOrder> {

    /**
     * 分页查询销售订单
     */
    JsonVO<PageDTO<SaleOrderListDTO>> page(SaleOrderQuery query);

    /**
     * 查询销售单
     * @param saleId 销售单id
     * @return 返回销售单数据
     */
    JsonVO<SaleOrderGenerateDTO> generateSaleOrderData(String saleId);

    /**
     * 查询采购单
     * @param purchaseId 采购单id
     * @return 返回采购单数据
     */
    JsonVO<PurchaseOrderGenerateDTO> generatePurchaseOrderData(String purchaseId);

    Boolean saleOrderDelete(List<String> ids);

    Boolean saleOrderAdd(SaleOrderListDTO dto);

    Boolean saleOrderChange(SaleOrderListDTO dto);

    Boolean verifyOrder(SaleOrderVerifyDTO request);


    /**
     * 从CSV文件导入销售订单
     */
    SaleOrderImportResult importFromExcel(MultipartFile file);

    /**
     * 导出简单报表（仅订单主表核心信息）
     */
    byte[] exportSimple(List<String> billNos);

    /**
     * 导出详细报表（订单主表+商品明细关联信息）
     */
    byte[] exportDetail(List<String> billNos);

    /**
     * 查询简单报表所需数据（供导出功能调用）
     */
    List<SaleOrderExportSimple> getSimpleExportData(List<String> billNos);

    /**
     * 查询详细报表所需数据（供导出功能调用，含明细）
     */
    List<SaleOrderExportDetail> getDetailExportData(List<String> billNos);

    /**
     * 将xlsx文件转成对应类
     */
    List<SaleOrderImport> parseExcelToDTO(MultipartFile file) throws IOException;
}