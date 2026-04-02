package com.zeroone.star.project.vo.j6.payment_order;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款单+核销单导出简单报表VO（用于导出简单Excel报表）
 */
@Data
public class OmySimpleVO {
    @ExcelProperty("所属组织")
    private String frame; // 所属组织
    @ExcelProperty("供应商")
    private String supplier; // 供应商
    @ExcelProperty("单据时间")
    private LocalDateTime time; // 单据时间
    @ExcelProperty("单据编号")
    private String number; // 单据编号
    @ExcelProperty("单据金额")
    private BigDecimal total; // 单据金额
    @ExcelProperty("核销金额")
    private BigDecimal writeOffAmount; // 核销金额（核销单）
    @ExcelProperty("关联人员")
    private String people; // 关联人员
    @ExcelProperty("审核状态")
    private String examineDesc; // 审核状态描述
    @ExcelProperty("核销状态")
    private String nucleusDesc; // 核销状态描述
    @ExcelProperty("制单人")
    private String user; // 制单人
    @ExcelProperty("备注信息")
    private String data; // 备注信息
}