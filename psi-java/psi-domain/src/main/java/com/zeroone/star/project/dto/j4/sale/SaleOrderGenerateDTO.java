package com.zeroone.star.project.dto.j4.sale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 描述：生成销售订单数据传输对象
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("生成销售订单数据传输对象")
public class SaleOrderGenerateDTO {

    @ApiModelProperty(value = "主键ID", example = "1")
    private String id;

    @ApiModelProperty(value = "关联单据", example = "SOR")
    private String source;

    @ApiModelProperty(value = "所属组织", example = "01星球")
    private String frame;

    @ApiModelProperty(value = "客户名称", example = "张三")
    private String customer;

    @ApiModelProperty(value = "单据时间", example = "2025-10-21T12:00:00")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号", example = "XSD202410210001")
    private String number;

    @ApiModelProperty(value = "单据金额", example = "1000.00")
    private BigDecimal total;

    @ApiModelProperty(value = "实际金额", example = "980.00")
    private BigDecimal actual;

    @ApiModelProperty(value = "实收金额", example = "980.00")
    private BigDecimal money;

    @ApiModelProperty(value = "单据费用", example = "0.0000")
    private BigDecimal cost;

    @ApiModelProperty(value = "结算账户", example = "招商银行")
    private String account;

    @ApiModelProperty(value = "关联人员", example = "李四")
    private String people;

    @ApiModelProperty(value = "物流信息", example = "顺丰速运 SF1234567890")
    private String logistics;

    @ApiModelProperty(value = "单据附件", example = "附件URL")
    private String file;

    @ApiModelProperty(value = "备注信息", example = "加急订单")
    private String data;

    @ApiModelProperty(value = "扩展信息", example = "{}")
    private String more;

    @NotNull
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "1")
    private Integer examine;

    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]", example = "0")
    private Integer nucleus;

    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]", example = "0")
    private Integer cse;

    @ApiModelProperty(value = "发票状态[0:未开票|1:部分开票|2:已开票|3:无需开票]", example = "0")
    private Integer invoice;

    @ApiModelProperty(value = "核对状态[0:未核对|1:已核对]", example = "0")
    private Integer check;

    @ApiModelProperty(value = "制单人", example = "admin")
    private String user;
}
    