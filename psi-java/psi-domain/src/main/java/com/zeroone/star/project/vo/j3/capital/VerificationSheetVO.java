package com.zeroone.star.project.vo.j3.capital;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Author: a
 * @CreateTime: 2025-10-18
 * @Description: 资金管理-核销单DTO
 * @Version: 1.0
 */
@Data
@ApiModel("核销单VO")
public class VerificationSheetVO {
    /**
     * 客户
     */
    @ApiModelProperty(value = "客户", example = "25")
    @NotBlank(message = "客户不能为空")
    private String customer;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商", example = "40")
    @NotBlank(message = "供应商不能为空")
    private String supplier;

    /**
     * 单据日期
     */
    @ApiModelProperty(value = "单据日期", required = true, example = "2022-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate time;


    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号", example = "HXD20231201001")
    @NotBlank(message = "单据编号不能为空")
    private String number;

    /**
     * 核销类型[0:预收冲应收|1:预付冲应付|2:应收冲应付|3:销退冲销售|4:购退冲采购]
     */
    @ApiModelProperty(value = "核销类型[0:预收冲应收|1:预付冲应付|2:应收冲应付|3:销退冲销售|4:购退冲采购]", example = "0")
    @NotBlank(message = "核销类型不能为空")
    @Pattern(regexp = "^[01234]$", message = "核销类型只能为0、1、2、3、4")
    private Integer type;

    /**
     * 总核金额
     */
    @ApiModelProperty(value = "总核金额", example = "100.00")
    @NotBlank(message = "总核金额不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "总核金额不能小于0")
    private BigDecimal pmy;

    /**
     * 总销金额
     */
    @ApiModelProperty(value = "总销金额", example = "100.00")
    @NotBlank(message = "总销金额不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "总销金额不能小于0")
    private BigDecimal smp;

    /**
     * 关联人员
     */
    @ApiModelProperty(value = "关联人员", example = "张三")
    @Size(max = 32, message = "关联人员长度不能超过32个字符")
    private String people;

    /**
     * 单据附件
     */
    @ApiModelProperty(value = "单据附件", example = "单据附件")
    private String file;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "备注信息")
    @Size(max = 256, message = "备注信息长度不能超过256个字符")
    private String data;

    /**
     * 核销单详情
     */
    @ApiModelProperty(value = "核销单详情")
    @NotBlank(message = "核销单详情不能为空")
    private List<VerificationSheetDetailVO> detail;
}
