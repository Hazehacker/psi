package com.zeroone.star.project.dto.j5.procurementreport;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.zeroone.star.project.dto.j5.procurementreport.PaymentFormExcelLocalDateConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ContentRowHeight(20)
@HeadRowHeight(25)
public class PaymentFormExcelDTO {
    
    @ExcelProperty("单据类型")
    @ColumnWidth(15)
    private String type;
    
    @ExcelProperty("所属组织")
    @ColumnWidth(20)
    private String frame;
    
    @ExcelProperty("供应商")
    @ColumnWidth(20)
    private String supplier;
    
    @ExcelProperty(value = "单据时间", converter = PaymentFormExcelLocalDateConverter.class)
    @ColumnWidth(15)
    private LocalDate time;
    
    @ExcelProperty("单据编号")
    @ColumnWidth(20)
    private String number;
    
    @ExcelProperty("单据金额")
    @ColumnWidth(15)
    private BigDecimal total;
    
    @ExcelProperty("实际金额")
    @ColumnWidth(15)
    private BigDecimal actual;
    
    @ExcelProperty("单据付款")
    @ColumnWidth(15)
    private BigDecimal payment;
    
    @ExcelProperty("应付款余额")
    @ColumnWidth(15)
    private BigDecimal balance;
    
    @ExcelProperty("付款率")
    @ColumnWidth(12)
    private String rate;
    
    @ExcelProperty("核销状态")
    @ColumnWidth(15)
    private String nucleusText;
    
    @ExcelIgnore
    private Integer nucleus;
    
    @ExcelProperty("备注信息")
    @ColumnWidth(30)
    private String data;
    
    /**
     * 设置核销状态文字描述
     */
    public void setNucleus(Integer nucleus) {
        this.nucleus = nucleus;
        if (nucleus == null) {
            this.nucleusText = "";
        } else {
            switch (nucleus) {
                case 0:
                    this.nucleusText = "未核销";
                    break;
                case 1:
                    this.nucleusText = "部分核销";
                    break;
                case 2:
                    this.nucleusText = "已核销";
                    break;
                default:
                    this.nucleusText = "";
            }
        }
    }
}