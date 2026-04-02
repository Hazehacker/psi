package com.zeroone.star.project.dto.j4.sale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售单列表数据对象")
public class SaleListDTO {

    @ApiModelProperty(value = "销售单id", example = "1")
    private String id;

    @ApiModelProperty(value = "关联单据", example = "SOR001")
    private String source;

    @ApiModelProperty(value = "所属组织", example = "01星球")
    private String frame;

    @ApiModelProperty(value = "客户", example = "张三")
    private String customer;

    @ApiModelProperty(value = "单据时间", example = "2020-01-01")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号", example = "XS202301010001")
    private String number;

    @ApiModelProperty(value = "单据金额", example = "3600.00")
    private BigDecimal total;

    @ApiModelProperty(value = "实际金额", example = "3500.00")
    private BigDecimal actual;

    @ApiModelProperty(value = "实收金额", example = "3500.00")
    private BigDecimal money;

    @ApiModelProperty(value = "单据费用", example = "100.00")
    private BigDecimal cost;

    @ApiModelProperty(value = "结算账户", example = "现金账户")
    private String account;

    @ApiModelProperty(value = "关联人员", example = "李四")
    private String people;

    @ApiModelProperty(value = "物流信息", example = "顺丰快递")
    private String logistics;

    @ApiModelProperty(value = "单据附件", example = "attachment.pdf")
    private String file;

    @ApiModelProperty(value = "备注信息", example = "重要客户")
    private String data;

    @ApiModelProperty(value = "扩展信息", example = "扩展信息")
    private String more;

    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "1")
    private Integer examine;

    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]", example = "1")
    private Integer nucleus;

    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]", example = "2")
    private Integer cse;

    @ApiModelProperty(value = "发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]", example = "1")
    private Integer invoice;

    @ApiModelProperty(value = "核对状态[0:未核对|1:已核对]", example = "1")
    private Integer check;

    @ApiModelProperty(value = "制单人", example = "王五")
    private String user;
}