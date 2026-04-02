package com.zeroone.star.project.dto.j8.finance.otherincome;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 修改其他收入单数据对象
 */
@Data
@ApiModel("修改其他收入单数据对象")
public class ReOtherIncomeDTO {
    @ApiModelProperty(value = "单据ID", example = "123", required = true)
    private String id;

    @ApiModelProperty(value = "所属组织", example = "组织001", required = true)
    private String frame;

    @ApiModelProperty(value = "客户", example = "客户001")
    private String customer;

    @ApiModelProperty(value = "单据时间", example = "2025-10-22 11:30:00", required = true)
    private LocalDateTime time;

    @ApiModelProperty(value = "单据金额", example = "8888.88", required = true)
    private BigDecimal total;

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

    @ApiModelProperty(value = "收入详情列表", required = true)
    private List<ReOtherIncomeInfoDTO> info;
}
