package com.zeroone.star.project.dto.j6.payment_order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OmyAllDTO {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty(value = "所属单据",example = "S111")
    private String pid;

    @ApiModelProperty(value = "所属组织",example = "默认组织")
    private String frame = "默认组织";

    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]",example = "1")
    private Integer examine;

    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]",example = "0")
    private Integer nucleus;

    @ApiModelProperty(value = "制单人",example = "管理员")
    private String user = "管理员";

    @NotBlank(message = "请选择供应商")
    @ApiModelProperty(value = "供应商",example = "中创集团")
    private String supplier;

    @NotNull(message = "请选择单据日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "单据日期",example = "2025-10-22")
    private LocalDate time;

    @NotBlank(message = "请选择单据编号")
    @ApiModelProperty(value = "单据编号",example = "N111")
    private String number;

    @ApiModelProperty("单据金额自动计算，无需输入")
    private BigDecimal total;

    @ApiModelProperty(value = "关联人员",example = "jack")
    private String people;

    @ApiModelProperty(value = "备注信息",example = "新增付款单示例")
    private String data;

    @ApiModelProperty(value = "单据附件",example = "点击上传")
    private String file;

    /*@NotBlank(message = "数据表格第1行结算账户不可为空")
    @ApiModelProperty(value = "结算账户",example = "中创资金")
    private String account;

    @NotNull(message="数据表格第1行结算金额不正确")
    @ApiModelProperty(value = "结算金额",example = "1000")
    private BigDecimal money;

    @ApiModelProperty(value = "结算号",example = "S111")
    private String settle;

    @ApiModelProperty(value = "备注信息",example = "新增付款单示例")
    private String data1;*/

    private List<OmyInfoAddDTO> infoList;
}
