package com.zeroone.star.purchase.excel;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author 小阳
 * @date 2025/10/30
 * @description 用于导出采购单详情的Excel实体类
 */
@Data
@ExcelIgnoreUnannotated
public class BuyInfoSimpleExcel {

    @ExcelProperty("商品名称")
    private String goods;

    @ExcelProperty("规格型号")
    private String attr;

    @ExcelProperty("辅助属性")
    private String additionalAttr;

    @ExcelProperty("单位")
    private String unit;

    @ExcelProperty("仓库")
    private String warehouse;

    @ExcelProperty("单价")
    private BigDecimal price;

    @ExcelProperty("数量")
    private BigDecimal nums;

    @ExcelProperty("折扣率")
    private BigDecimal discount;

    @ExcelProperty("折扣额")
    private BigDecimal dsc;

    @ExcelProperty("金额")
    private BigDecimal total;

    @ExcelProperty("税率")
    private BigDecimal tax;

    @ExcelProperty("税额")
    private BigDecimal tat;

    @ExcelProperty("价税合计")
    private BigDecimal tpt;

    @ExcelProperty("备注信息")
    private String data;

    @ExcelProperty("退货数量")
    private BigDecimal retreat;

    // 不用导出字段
    private String id;

    private String pid;

    private String source;

    private String batch;

    private LocalDateTime mfd;

    private String serial;
}
