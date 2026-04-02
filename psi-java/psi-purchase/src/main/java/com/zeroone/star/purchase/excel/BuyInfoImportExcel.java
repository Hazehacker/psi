package com.zeroone.star.purchase.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

// 明细表Excel映射类
@Data
public class BuyInfoImportExcel {
    @ExcelProperty(index = 10) // 商品名称在K列
    private String productName;
    
    @ExcelProperty(index = 11) // 辅助属性在L列
    private String auxiliaryAttribute;
    
    @ExcelProperty(index = 12) // 单位在M列
    private String unit;
    
    @ExcelProperty(index = 13) // 仓库在N列
    private String warehouse;
    
    @ExcelProperty(index = 14) // 批次号在O列
    private String batchNumber;
    
    @ExcelProperty(index = 15) // 生产日期在P列
    private String productionDate;
    
    @ExcelProperty(index = 16) // 单价在Q列
    private BigDecimal unitPrice;
    
    @ExcelProperty(index = 17) // 数量在R列
    private BigDecimal quantity;

    @ExcelProperty(index = 18) //  序列号在S列
    private String serial;
    
    @ExcelProperty(index = 19) // 折扣率在T列
    private BigDecimal discountRate;
    
    @ExcelProperty(index = 20) // 折扣额在U列
    private BigDecimal discountAmount;
    
    @ExcelProperty(index = 21) // 金额在V列
    private BigDecimal amount;
    
    @ExcelProperty(index = 22) // 税率在W列
    private BigDecimal taxRate;
    
    @ExcelProperty(index = 23) // 税额在X列
    private BigDecimal taxAmount;
    
    @ExcelProperty(index = 24) // 价税合计在Y列
    private BigDecimal totalAmountWithTax;

    // 备注信息
    private String data;
}