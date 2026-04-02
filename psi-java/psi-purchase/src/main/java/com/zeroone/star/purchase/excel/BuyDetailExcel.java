package com.zeroone.star.purchase.excel;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zeroone.star.purchase.excel.converter.LocalDateToStringConverter;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
public class BuyDetailExcel {

    @ExcelProperty(value = "单据日期", converter = LocalDateToStringConverter.class)
    private LocalDateTime billDate;

    @ExcelProperty("单据编号")
    private String billNo;

    @ExcelProperty("单据金额")
    private BigDecimal billAmount;

    @ExcelProperty("实际金额")
    private BigDecimal actualAmount;

    @ExcelProperty("实付金额")
    private BigDecimal paidAmount;

    @ExcelProperty("结算账户")
    private String account;

    @ExcelProperty("关联人员")
    private String relatedPerson;

    @ExcelProperty("物流信息")
    private String logisticsInfo;

    @ExcelProperty("备注信息")
    private String remark;


    // 以下为不需要导出的属性
    private String id;

    /**
     * 关联单据|BOR
     */
    private String source;

    /**
     * 所属组织
     */
    private String frame;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 单据费用
     */
    private BigDecimal cost;

    /**
     * 单据附件
     */
    private String file;

    /**
     * 扩展信息
     */
    private String more;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    private Integer examine;

    /**
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     */
    private Integer nucleus;

    /**
     * 费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]
     */
    private Integer cse;

    /**
     * 发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]
     */
    private Integer invoice;

    /**
     * 核对状态[0:未核对|1:已核对]
     */
    private Boolean check;

    /**
     * 制单人
     */
    private String user;

}
