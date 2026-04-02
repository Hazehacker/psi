package com.zeroone.star.project.vo.j6.payment_order;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 付款单详细报表VO（用于导出Excel）
 */
@Data
public class OmyInfoDetailVO {
//    @ExcelProperty(value = "ID", index = 0)
//    private String id;
//
//    @ExcelProperty(value = "所属付款单ID", index = 1)
//    private String pid;

    @ExcelProperty(value = "结算账户", index = 0)
    private String account;

    @ExcelProperty(value = "结算金额", index = 1)
    private BigDecimal money;

    @ExcelProperty(value = "结算号", index = 2)
    private String settle;

    @ExcelProperty(value = "备注信息", index = 3)
    private String data;
}