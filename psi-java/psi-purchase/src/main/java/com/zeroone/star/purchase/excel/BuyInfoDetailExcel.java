package com.zeroone.star.purchase.excel;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ExcelIgnoreUnannotated
public class BuyInfoDetailExcel implements Serializable {

    private static final long serialVersionUID = 1L;


    @ExcelProperty("商品名称")
    private String goods;

    @ExcelProperty("规格型号")
    private String spec;

    @ExcelProperty("辅助属性")
    private String attr;

    @ExcelProperty("单位")
    private String unit;

    @ExcelProperty("仓库")
    private String warehouse;

    @ExcelProperty("单价")
    private BigDecimal price;

    @ExcelProperty("数量")
    private BigDecimal nums;

    @ExcelProperty("折扣率(%)")
    private BigDecimal discount;

    @ExcelProperty("折扣额")
    private BigDecimal dsc;

    @ExcelProperty("金额")
    private BigDecimal total;

    @ExcelProperty("备注信息")
    private String data;

}
