package com.zeroone.star.purchase.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

// 主表Excel映射类
@Data
public class BuyImportExcel {

    /**
     * 供应商
     */
    private String supplier;

    @ExcelProperty(index = 1) // 单据日期在B列
    private String documentDate;
    
    @ExcelProperty(index = 2) // 单据编号在C列
    private String documentNumber;
    
    @ExcelProperty(index = 3) // 单据金额在D列
    private BigDecimal documentAmount;
    
    @ExcelProperty(index = 4) // 实际金额在E列
    private BigDecimal actualAmount;
    
    @ExcelProperty(index = 5) // 实付金额在F列
    private BigDecimal paidAmount;

    /**
     * 结算账户
     */
    private String account;

    /**
     * 关联人员
     */
    private String people;

    @ExcelProperty(index = 8) // 物流信息在I列
    private String logisticsInfo;
    
    @ExcelProperty(index = 9) // 备注信息在J列
    private String remark;
}