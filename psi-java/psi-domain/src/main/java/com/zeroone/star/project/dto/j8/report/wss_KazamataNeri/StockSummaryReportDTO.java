package com.zeroone.star.project.dto.j8.report.wss_KazamataNeri;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("商品收发汇总表详细信息")
public class StockSummaryReportDTO extends ExcelDTO {
    /* ========= 参数字段 ========= */
    @ExcelIgnore
    @ApiModelProperty(value = "仓库参数id")
    private String warehouseNumber;

    /* ========= 前5列：直接字段导出 ========= */
    @ExcelProperty(value = "商品名称", index = 0)
    @ApiModelProperty(value = "商品名称", example = "刀具")
    private String name;

    @ExcelProperty(value = "仓库", index = 1)
    @ApiModelProperty(value = "仓库信息", example = "北仓")
    private String warehouse;

    @ExcelProperty(value = "商品编号", index = 2)
    @ApiModelProperty(value = "商品编号", example = "0001")
    private String number;

    @ExcelProperty(value = "规格型号", index = 3)
    @ApiModelProperty(value = "规格型号", example = "DJ-0001")
    private String spec;

    @ExcelProperty(value = "商品单位", index = 4)
    @ApiModelProperty(value = "商品单位", example = "个")
    private String unit;

    /* ========= 子DTO，仅参与组装，不导出 ========= */
    @ExcelIgnore
    @ApiModelProperty("采购单(商品收发汇总表)")
    private BuyDTO buyDTO;

    @ExcelIgnore
    @ApiModelProperty("采购退货单(商品收发汇总表)")
    private BreDTO breDTO;

    @ExcelIgnore
    @ApiModelProperty("期初(商品收发汇总表)")
    private RoomDTO roomDTO;

    @ExcelIgnore
    @ApiModelProperty("其它入库单(商品收发汇总表)")
    private EntryDTO entryDTO;

    @ExcelIgnore
    @ApiModelProperty("其它出库单(商品收发汇总表)")
    private ExtryDTO extryDTO;

    @ExcelIgnore
    @ApiModelProperty("销售单(商品收发汇总表)")
    private SellDTO sellDTO;

    @ExcelIgnore
    @ApiModelProperty("销售退货单(商品收发汇总表)")
    private SreDTO sreDTO;

    @ExcelIgnore
    @ApiModelProperty("调拨单-入(商品收发汇总表)")
    private SwapStorehouseDTO swapStorehouseDTO;

    @ExcelIgnore
    @ApiModelProperty("调拨单-出(商品收发汇总表)")
    private SwapWarehouseDTO swapWarehouseDTO;

    @ExcelIgnore
    @ApiModelProperty("时间")
    private LocalDateTime time;

    /* ========= 导出列：全部字段导出 ========= */
    // 期初
    @ApiModelProperty("期初成本")
    @ExcelProperty(value = "期初成本", index = 5)
    private BigDecimal roomPrice;
    @ApiModelProperty("期初数量")
    @ExcelProperty(value = "期初数量", index = 6)
    private BigDecimal roomNums;
    @ApiModelProperty("期初总成本")
    @ExcelProperty(value = "期初总成本", index = 7)
    private BigDecimal roomTotal;

    // 采购
    @ExcelProperty(value = "采购成本", index = 8)
    private BigDecimal buyPrice;
    @ExcelProperty(value = "采购数量", index = 9)
    private BigDecimal buyNums;
    @ExcelProperty(value = "采购总成本", index = 10)
    private BigDecimal buyTotal;

    // 采购退货（出）
    @ExcelProperty(value = "采购退货成本", index = 11)
    private BigDecimal brePrice;
    @ExcelProperty(value = "采购退货数量", index = 12)
    private BigDecimal breNums;
    @ExcelProperty(value = "采购退货总成本", index = 13)
    private BigDecimal breTotal;

    // 销售（出）
    @ExcelProperty(value = "销售成本", index = 14)
    private BigDecimal sellPrice;
    @ExcelProperty(value = "销售数量", index = 15)
    private BigDecimal sellNums;
    @ExcelProperty(value = "销售总成本", index = 16)
    private BigDecimal sellTotal;

    // 销售退货（入）
    @ExcelProperty(value = "销售退货成本", index = 17)
    private BigDecimal srePrice;
    @ExcelProperty(value = "销售退货数量", index = 18)
    private BigDecimal sreNums;
    @ExcelProperty(value = "销售退货总成本", index = 19)
    private BigDecimal sreTotal;

    // 调拨出（出）
    @ExcelProperty(value = "调拨出库成本", index = 20)
    private BigDecimal swapOutPrice;
    @ExcelProperty(value = "调拨出库数量", index = 21)
    private BigDecimal swapOutNums;
    @ExcelProperty(value = "调拨出库总成本", index = 22)
    private BigDecimal swapOutTotal;

    // 调拨入（入）
    @ExcelProperty(value = "调拨入库成本", index = 23)
    private BigDecimal swapInPrice;
    @ExcelProperty(value = "调拨入库数量", index = 24)
    private BigDecimal swapInNums;
    @ExcelProperty(value = "调拨入库总成本", index = 25)
    private BigDecimal swapInTotal;

    // 其它出库（出）
    @ExcelProperty(value = "其它出库成本", index = 26)
    private BigDecimal extryPrice;
    @ExcelProperty(value = "其它出库数量", index = 27)
    private BigDecimal extryNums;
    @ExcelProperty(value = "其它出库总成本", index = 28)
    private BigDecimal extryTotal;

    // 其它入库（入）
    @ExcelProperty(value = "其它入库成本", index = 29)
    private BigDecimal entryPrice;
    @ExcelProperty(value = "其它入库数量", index = 30)
    private BigDecimal entryNums;
    @ExcelProperty(value = "其它入库总成本", index = 31)
    private BigDecimal entryTotal;

    // 汇总
    @ApiModelProperty("汇总成本")
    @ExcelProperty(value = "汇总成本", index = 32)
    private BigDecimal SumPrice;
    @ApiModelProperty("汇总数量")
    @ExcelProperty(value = "汇总数量", index = 33)
    private BigDecimal SumNums;
    @ApiModelProperty("汇总总成本")
    @ExcelProperty(value = "汇总总成本", index = 34)
    private BigDecimal SumTotal;

    /* ========= 计算逻辑 ========= */
    private static BigDecimal z(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    /**
     * 每次子DTO/字段变动后调用，自动重算汇总
     */
    private void recomputeSum() {
        // 期末数量 = 期初 + 入(采购/销退/调入/其它入) - 出(采退/销售/调出/其它出)
        BigDecimal inNums = z(buyNums).add(z(sreNums)).add(z(swapInNums)).add(z(entryNums));
        BigDecimal outNums = z(breNums).add(z(sellNums)).add(z(swapOutNums)).add(z(extryNums));
        this.SumNums = z(roomNums).add(inNums).subtract(outNums);

        // 期末金额 = 期初 + 入 - 出（全部成本口径）
        BigDecimal inTotal = z(buyTotal).add(z(sreTotal)).add(z(swapInTotal)).add(z(entryTotal));
        BigDecimal outTotal = z(breTotal).add(z(sellTotal)).add(z(swapOutTotal)).add(z(extryTotal));
        this.SumTotal = z(roomTotal).add(inTotal).subtract(outTotal);

        // 期末单价
        if (SumNums != null && SumNums.compareTo(BigDecimal.ZERO) != 0 && SumTotal != null) {
            this.SumPrice = SumTotal.divide(SumNums, 4, java.math.RoundingMode.HALF_UP);
        } else {
            this.SumPrice = null;
        }
    }

    /**
     * 导出前可显式调用一次兜底（可选）
     */
    public void finalizeForExport() {
        recomputeSum();
    }

    /* ========= 自定义 setter：当设置子DTO时，自动把值铺到导出字段并重算 ========= */
    public void setRoomDTO(RoomDTO roomDTO) {
        this.roomDTO = roomDTO;
        if (roomDTO != null) {
            this.roomPrice = roomDTO.getPrice();
            this.roomNums = roomDTO.getNums();
            this.roomTotal = roomDTO.getTotal();
        }
        recomputeSum();
    }

    public void setBuyDTO(BuyDTO buyDTO) {
        this.buyDTO = buyDTO;
        if (buyDTO != null) {
            this.buyPrice = buyDTO.getPrice();
            this.buyNums = buyDTO.getNums();
            this.buyTotal = buyDTO.getTotal();
        }
        recomputeSum();
    }

    public void setBreDTO(BreDTO breDTO) {
        this.breDTO = breDTO;
        if (breDTO != null) {
            this.brePrice = breDTO.getPrice();
            this.breNums = breDTO.getNums();
            this.breTotal = breDTO.getTotal();
        }
        recomputeSum();
    }

    public void setSellDTO(SellDTO sellDTO) {
        this.sellDTO = sellDTO;
        if (sellDTO != null) {
            this.sellPrice = sellDTO.getPrice();
            this.sellNums = sellDTO.getNums();
            this.sellTotal = sellDTO.getTotal();
        }
        recomputeSum();
    }

    public void setSreDTO(SreDTO sreDTO) {
        this.sreDTO = sreDTO;
        if (sreDTO != null) {
            this.srePrice = sreDTO.getPrice();
            this.sreNums = sreDTO.getNums();
            this.sreTotal = sreDTO.getTotal();
        }
        recomputeSum();
    }

    public void setSwapWarehouseDTO(SwapWarehouseDTO swapWarehouseDTO) {
        this.swapWarehouseDTO = swapWarehouseDTO;
        if (swapWarehouseDTO != null) {
            this.swapOutPrice = swapWarehouseDTO.getPrice();
            this.swapOutNums = swapWarehouseDTO.getNums();
            this.swapOutTotal = swapWarehouseDTO.getTotal();
        }
        recomputeSum();
    }

    public void setSwapStorehouseDTO(SwapStorehouseDTO swapStorehouseDTO) {
        this.swapStorehouseDTO = swapStorehouseDTO;
        if (swapStorehouseDTO != null) {
            this.swapInPrice = swapStorehouseDTO.getPrice();
            this.swapInNums = swapStorehouseDTO.getNums();
            this.swapInTotal = swapStorehouseDTO.getTotal();
        }
        recomputeSum();
    }

    public void setExtryDTO(ExtryDTO extryDTO) {
        this.extryDTO = extryDTO;
        if (extryDTO != null) {
            this.extryPrice = extryDTO.getPrice();
            this.extryNums = extryDTO.getNums();
            this.extryTotal = extryDTO.getTotal();
        }
        recomputeSum();
    }

    public void setEntryDTO(EntryDTO entryDTO) {
        this.entryDTO = entryDTO;
        if (entryDTO != null) {
            this.entryPrice = entryDTO.getPrice();
            this.entryNums = entryDTO.getNums();
            this.entryTotal = entryDTO.getTotal();
        }
        recomputeSum();
    }

    /* 如果装配层直接 set 数值字段，也自动重算（可选） */
    public void setRoomPrice(BigDecimal v) {
        this.roomPrice = v;
        recomputeSum();
    }

    public void setRoomNums(BigDecimal v) {
        this.roomNums = v;
        recomputeSum();
    }

    public void setRoomTotal(BigDecimal v) {
        this.roomTotal = v;
        recomputeSum();
    }

    public void setBuyPrice(BigDecimal v) {
        this.buyPrice = v;
        recomputeSum();
    }

    public void setBuyNums(BigDecimal v) {
        this.buyNums = v;
        recomputeSum();
    }

    public void setBuyTotal(BigDecimal v) {
        this.buyTotal = v;
        recomputeSum();
    }

    public void setBrePrice(BigDecimal v) {
        this.brePrice = v;
        recomputeSum();
    }

    public void setBreNums(BigDecimal v) {
        this.breNums = v;
        recomputeSum();
    }

    public void setBreTotal(BigDecimal v) {
        this.breTotal = v;
        recomputeSum();
    }

    public void setSellPrice(BigDecimal v) {
        this.sellPrice = v;
        recomputeSum();
    }

    public void setSellNums(BigDecimal v) {
        this.sellNums = v;
        recomputeSum();
    }

    public void setSellTotal(BigDecimal v) {
        this.sellTotal = v;
        recomputeSum();
    }

    public void setSrePrice(BigDecimal v) {
        this.srePrice = v;
        recomputeSum();
    }

    public void setSreNums(BigDecimal v) {
        this.sreNums = v;
        recomputeSum();
    }

    public void setSreTotal(BigDecimal v) {
        this.sreTotal = v;
        recomputeSum();
    }

    public void setSwapOutPrice(BigDecimal v) {
        this.swapOutPrice = v;
        recomputeSum();
    }

    public void setSwapOutNums(BigDecimal v) {
        this.swapOutNums = v;
        recomputeSum();
    }

    public void setSwapOutTotal(BigDecimal v) {
        this.swapOutTotal = v;
        recomputeSum();
    }

    public void setSwapInPrice(BigDecimal v) {
        this.swapInPrice = v;
        recomputeSum();
    }

    public void setSwapInNums(BigDecimal v) {
        this.swapInNums = v;
        recomputeSum();
    }

    public void setSwapInTotal(BigDecimal v) {
        this.swapInTotal = v;
        recomputeSum();
    }

    public void setExtryPrice(BigDecimal v) {
        this.extryPrice = v;
        recomputeSum();
    }

    public void setExtryNums(BigDecimal v) {
        this.extryNums = v;
        recomputeSum();
    }

    public void setExtryTotal(BigDecimal v) {
        this.extryTotal = v;
        recomputeSum();
    }

    public void setEntryPrice(BigDecimal v) {
        this.entryPrice = v;
        recomputeSum();
    }

    public void setEntryNums(BigDecimal v) {
        this.entryNums = v;
        recomputeSum();
    }

    public void setEntryTotal(BigDecimal v) {
        this.entryTotal = v;
        recomputeSum();
    }
}
