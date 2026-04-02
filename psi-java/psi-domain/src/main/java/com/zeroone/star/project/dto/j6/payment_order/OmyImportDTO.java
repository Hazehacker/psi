package com.zeroone.star.project.dto.j6.payment_order;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 付款单详情导入DTO（接收json导入数据）
 * 批量导入DTO
 */
@Data
public class OmyImportDTO {
    // 主键ID由后端生成，不需要前端传入
    private String id;

    // 所属付款单ID
    private String pid;

    // 结算账户（允许为null）
    @ExcelProperty(value = "结算账户", index = 0)
    private String account;

    // 结算金额（非空，保留验证）
    @NotBlank(message = "结算金额不能为空")
    @ExcelProperty(value = "结算金额", index = 1)
    private BigDecimal money;

    // 结算号（非空，保留验证）
    @NotBlank(message = "结算号不能为空")
    @ExcelProperty(value = "结算号", index = 2)
    private String settle;

    // 备注信息（允许为null，移除@NotBlank）
    @ExcelProperty(value = "备注信息", index = 3)
    private String data;
}