package com.zeroone.star.project.dto.j3.capital;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("修改核销单DTO")
public class VerificationSheetModifyDTO{

    @ApiModelProperty(value = "核销单id", example = "1572864789012345678")
    @NotBlank(message = "核销单id不能为空")
    private String id;

    /**
     * 客户
     */
    @ApiModelProperty(value = "客户", example = "25")
    private String customer;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商", example = "40")
    private String supplier;

    /**
     * 单据日期
     */
    @ApiModelProperty(value = "单据日期", required = true, example = "2022-01-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @NotNull(message = "单据日期不能为空")
    private LocalDateTime time;


    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号", required = true, example = "HXD20231201001")
    @NotBlank(message = "单据编号不能为空")
    private String number;

    /**
     * 核销类型[0:预收冲应收|1:预付冲应付|2:应收冲应付|3:销退冲销售|4:购退冲采购]
     */
    @ApiModelProperty(value = "核销类型[0:预收冲应收|1:预付冲应付|2:应收冲应付|3:销退冲销售|4:购退冲采购]", required = true, example = "0")
    @NotNull(message = "核销类型不能为空")
    @Range(min = 0, max = 4, message = "核销类型只能为0、1、2、3、4")
    private Integer type;

    /**
     * 总核金额
     */
    @ApiModelProperty(value = "总核金额", required = true, example = "100.00")
    @NotNull(message = "总核金额不能为空")
    @DecimalMin(value = "0.0", message = "总核金额不能小于0")
    private BigDecimal pmy;

    /**
     * 总销金额
     */
    @ApiModelProperty(value = "总销金额", required = true, example = "100.00")
    @NotNull(message = "总销金额不能为空")
    @DecimalMin(value = "0.0", message = "总销金额不能小于0")
    private BigDecimal smp;

    /**
     * 关联人员
     */
    @ApiModelProperty(value = "关联人员", example = "李四")
    private String people;

    /**
     * 单据附件
     */
    @ApiModelProperty(value = "单据附件", example = "test.png")
    private String file;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "暂无")
    private String data;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "0")
    @NotNull(message = "审核状态不能为空")
    @Range(min = 0, max = 1, message = "审核状态只能为0、1")
    private Integer examine;

    /**
     * 核销单详情
     */
    @ApiModelProperty(value = "核销单详情")
    @NotEmpty(message = "核销单详情不能为空")
    private List<VerificationSheetModifyDetailDTO> detail;
}
