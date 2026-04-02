package com.zeroone.star.report.mapper.StockSummaryReport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j8.report.wss_KazamataNeri.*;
import com.zeroone.star.project.query.j8.report.StockSummaryReportQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockSummaryReportMapper {
    // 子明细保持不变
    BreDTO findBreByGoods(StockSummaryReportDTO dto);

    BuyDTO findBuyByGoods(StockSummaryReportDTO dto);

    EntryDTO findEntryByGoods(StockSummaryReportDTO dto);   // 其它入库

    ExtryDTO findExtryByGoods(StockSummaryReportDTO dto);   // 其它出库

    RoomDTO findRoomByGoods(StockSummaryReportDTO dto);

    SellDTO findSellByGoods(StockSummaryReportDTO dto);

    SreDTO findSreByGoods(StockSummaryReportDTO dto);

    // 调拨：入 / 出（方法名与 DTO 对齐，避免歧义）
    SwapStorehouseDTO findSwapStorehouseByGoods(StockSummaryReportDTO dto); // 入

    SwapWarehouseDTO findSwapWarehouseByGoods(StockSummaryReportDTO dto); // 出  ← 原来叫 findWarehouseByGoods，建议改名

    // 商品主列表：分页签名
    Page<StockSummaryReportDTO> findGoods(Page<?> page, @Param("q") StockSummaryReportQuery query);

    // 商品主列表（导出用，不分页）
    List<StockSummaryReportDTO> findGoodsAll(@Param("q") StockSummaryReportQuery query);

}
