package com.zeroone.star.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j4.sale.PurchaseOrderGenerateDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderGenerateDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderListDTO;
import com.zeroone.star.project.query.j4.sale.SaleOrderQuery;
import com.zeroone.star.sale.entity.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SaleOrderMapper extends BaseMapper<SaleOrder> {

    /**
     * 查询销售订单分页数据（带条件）
     * @return 分页后的订单列表DTO
     */
    Page<SaleOrderListDTO> list(Page<SaleOrderListDTO> page, @Param("query") SaleOrderQuery query);

    /**
     * 根据查询条件查询销售订单
     * @param saleOrder 查询条件
     * @return 销售订单列表
     */
    List<SaleOrder> getByQuery(SaleOrder saleOrder);

    /**
     * 根据 ID 查询销售单
     * @param saleId 销售单主键 ID
     * @return 销售单数据
     */
    SaleOrderGenerateDTO selectSaleById(String saleId);

    PurchaseOrderGenerateDTO selectPurchaseById(String purchaseId);

    /**
     * 批量插入销售订单主表数据
     */
    int batchInsert(@Param("list") List<SaleOrder> list);

    /**
     * 批量插入销售订单明细表数据
     * @param list 销售订单明细实体列表
     * @return 插入成功的条数
     */
    int batchInfoInsert(@Param("list") List<SaleOrderInfo> list);

    /**
     * 校验单据编号是否重复（查询已存在的编号）
     */
    List<String> checkDuplicateNumber(@Param("numbers") List<String> numbers);

    /**
     * 查询简洁导出数据（用于简单导出场景）
     */
    List<SaleOrderExportSimple> selectSimpleExportByNumbers(@Param("numbers") List<String> numbers);

    /**
     * 查询详细导出的主表数据
     */
    List<SaleOrderExportDetail> selectDetailExportMain(@Param("numbers") List<String> numbers);

    /**
     * 查询详细导出的明细数据（关联主表）

     */
    List<SaleOrderExportDetailItem> selectDetailExportItems(@Param("orderNos") List<String> orderNos);

}