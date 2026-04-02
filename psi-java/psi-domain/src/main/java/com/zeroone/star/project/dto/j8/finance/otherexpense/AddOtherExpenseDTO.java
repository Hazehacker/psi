package com.zeroone.star.project.dto.j8.finance.otherexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 新增其他支出单数据对象
 * 涉及数据表: oce
 */
@Data
@ApiModel("新增其他收入单数据对象")
public class AddOtherExpenseDTO {
    @ApiModelProperty(value = "所属组织", example = "组织001", required = true)
    private String frame;

    @ApiModelProperty(value = "客户", example = "客户001")
    private String customer;

    @ApiModelProperty(value = "单据时间", example = "2025-10-22 11:30:00", required = true)
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号", example = "QTZCD2510232309059")
    private String number;

    @ApiModelProperty(value = "单据金额", example = "8888.88", required = true)
    private BigDecimal total;

    @ApiModelProperty(value = "实际金额", example = "12345.67")
    private BigDecimal actual;

    @ApiModelProperty(value = "实付金额/单据收款", example = "12345.67")
    private BigDecimal money;

    @ApiModelProperty(value = "结算账户", example = "ICBC001", required = true)
    private String account;

    @ApiModelProperty(value = "关联人员", example = "people002")
    private String people;

    @ApiModelProperty(value = "单据附件", example = "[\"/upload/income_receipt_20251022.jpg\"]")
    private String file;

    @ApiModelProperty(value = "备注信息", example = "这是一笔其它收入")
    private String data;

    @ApiModelProperty(value = "扩展信息", example = "xyz")
    private String more;

    @ApiModelProperty(value = "支出详情列表", required = true)
    private List<AddOtherExpenseInfoDTO> info;
}
