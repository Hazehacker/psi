package com.zeroone.star.project.dto.j6.payment_order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel("主表DTO")
public class OmyDTO {
    //这个是公共部分
    @ApiModelProperty("id")
    private String id;

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
//-----------------------------------------------------------------------------
    //这个是获取主表列表拥有的特殊部分
    @ApiModelProperty(value = "所属组织",example = "默认组织")
    private String frame = "默认组织";

    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]",example = "1")
    private Integer examine;

    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]",example = "0")
    private Integer nucleus;

    @ApiModelProperty(value = "制单人",example = "管理员")
    private String user = "管理员";
//------------------------------------------------------------------------------
    @ApiModelProperty(value = "单据附件",example = "点击上传")
    private String file;

    /*@ApiModelProperty(value = "扩展信息")
    private String more;*/



}
