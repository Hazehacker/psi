package com.zeroone.star.purchase.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购退货单导入数据Excel类
 *
 * @author Hazenix 
 * @since 2025-11-13
 */
@Data
public class PurchaseReturnImportExcel {

    // 第一级表头：单据资料
    @ExcelProperty(value = {"单据资料", "供应商"}, index = 0)
    private String supplier;

    @ExcelProperty(value = {"单据资料", "单据日期"}, index = 1)
    private String time;

    @ExcelProperty(value = {"单据资料", "单据编号"}, index = 2)
    private String number;

    @ExcelProperty(value = {"单据资料", "单据金额"}, index = 3)
    private BigDecimal total;

    @ExcelProperty(value = {"单据资料", "实际金额"}, index = 4)
    private BigDecimal actual;

    @ExcelProperty(value = {"单据资料", "实收金额"}, index = 5)
    private BigDecimal money;

    @ExcelProperty(value = {"单据资料", "结算账户"}, index = 6)
    private String account;

    @ExcelProperty(value = {"单据资料", "关联人员"}, index = 7)
    private String people;

    @ExcelProperty(value = {"单据资料", "物流信息"}, index = 8)
    private String logistics;

    @ExcelProperty(value = {"单据资料", "备注信息"}, index = 9)
    private String data;

    // 第二级表头：商品资料
    @ExcelProperty(value = {"商品资料", "商品名称"}, index = 10)
    private String goods;

    @ExcelProperty(value = {"商品资料", "辅助属性"}, index = 11)
    private String attr;

    @ExcelProperty(value = {"商品资料", "单位"}, index = 12)
    private String unit;

    @ExcelProperty(value = {"商品资料", "仓库"}, index = 13)
    private String warehouse;

    @ExcelProperty(value = {"商品资料", "批次号"}, index = 14)
    private String batch;

    @ExcelProperty(value = {"商品资料", "生产日期"}, index = 15)
    private String mfd;

    @ExcelProperty(value = {"商品资料", "单价"}, index = 16)
    private BigDecimal price;

    @ExcelProperty(value = {"商品资料", "数量"}, index = 17)
    private BigDecimal nums;

    @ExcelProperty(value = {"商品资料", "序列号"}, index = 18)
    private String serial;

    @ExcelProperty(value = {"商品资料", "折扣率(%)"}, index = 19)
    private BigDecimal discount;

    @ExcelProperty(value = {"商品资料", "折扣额"}, index = 20)
    private BigDecimal dsc;

    @ExcelProperty(value = {"商品资料", "金额"}, index = 21)
    private BigDecimal total_goods;

    @ExcelProperty(value = {"商品资料", "税率(%)"}, index = 22)
    private BigDecimal tax;

    @ExcelProperty(value = {"商品资料", "税额"}, index = 23)
    private BigDecimal tat;

    @ExcelProperty(value = {"商品资料", "价税合计"}, index = 24)
    private BigDecimal tpt;

    @ExcelProperty(value = {"商品资料", "备注信息"}, index = 25)
    private String data_goods;

}