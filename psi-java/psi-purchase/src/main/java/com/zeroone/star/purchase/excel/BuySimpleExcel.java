package com.zeroone.star.purchase.excel;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zeroone.star.purchase.excel.converter.LocalDateToStringConverter;
import com.zeroone.star.purchase.excel.converter.StatusToStringConverter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * author: 小阳
 * date: 2025/10/29
 * description: 采购单excel实体类
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
public class BuySimpleExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("所属组织")
    private String frame;

    @ExcelProperty("供应商")
    private String supplier;

    @ExcelProperty(value = "单据时间", converter = LocalDateToStringConverter.class)
    private LocalDateTime time;

    @ExcelProperty("单据编号")
    private String number;

    @ExcelProperty("单据金额")
    private BigDecimal total;

    @ExcelProperty("实际金额")
    private BigDecimal actual;

    @ExcelProperty("单据付款")
    private BigDecimal money;

    @ExcelProperty("核销金额")
    private BigDecimal writeOff;

    @ExcelProperty("单据费用")
    private BigDecimal cost;

    @ExcelProperty("关联人员")
    private String people;

    @ExcelProperty(value = "审核状态", converter = StatusToStringConverter.class)
    private Integer examine;

    @ExcelProperty(value = "核销状态", converter = StatusToStringConverter.class)
    private Integer nucleus;

    @ExcelProperty(value = "费用状态", converter = StatusToStringConverter.class)
    private Integer cse;

    @ExcelProperty(value = "发票状态", converter = StatusToStringConverter.class)
    private Integer invoice;

    @ExcelProperty(value = "核对状态")
    private Boolean check;

    @ExcelProperty("制单人")
    private String user;

    @ExcelProperty("备注信息")
    private String data;

    // 不用导出的信息
    private String id;

    private String source;

    private String account;

    private String logistics;

    private String file;

    private String more;

}