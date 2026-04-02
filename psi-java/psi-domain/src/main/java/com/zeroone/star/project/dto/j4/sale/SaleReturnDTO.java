package com.zeroone.star.project.dto.j4.sale;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Api(value = "销售退单参数")
public class SaleReturnDTO {
    @ApiModelProperty(value = "销售退货订单ID", example = "1")
    private String id;
    @ApiModelProperty(value = "所属组织",example = "01星球")
    private String frame;
    @ApiModelProperty(value = "客户",example = "张三")
    private String customer;
    @ApiModelProperty(value = "单据时间",example = "2020-01-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate time;
    @ApiModelProperty(value = "单据编号",example = "123456789")
    private String number;
    @ApiModelProperty(value = "单据金额",example = "36.0")
    private BigDecimal total;
    @ApiModelProperty(value = "实际金额",example = "36.0")
    private BigDecimal actual;
    @ApiModelProperty(value = "实付金额", example = "3.0")
    private BigDecimal money;
    @ApiModelProperty(value = "单据费用", example = "36.0")
    private BigDecimal cost;
    @ApiModelProperty(value = "结算账户名称", example = "xxx")
    private String account;
    @ApiModelProperty(value = "关联人员",example = "李四")
    private String people;
    @ApiModelProperty(value = "物流信息",example = "物流信息")
    private String logistics;
    @ApiModelProperty(value = "单据附件",example = "单据附件")
    private String file;
    @ApiModelProperty(value = "备注信息",example = "备注信息")
    private String data;
    @ApiModelProperty(value = "扩展信息",example = "扩展信息")
    private String more;
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]",example = "1")
    private Integer examine;
    @ApiModelProperty(value = "核销状态 (0: 未核销 1: 部分核销 2: 已核销)", example = "2")
    private Integer nucleus;
    @ApiModelProperty(value = "费用状态(0: 未结算 1: 部分结算 2: 已结算 3: 无需结算)", example = "2")
    private Integer cse;
    @ApiModelProperty(value = "发票状态 (0: 未开票 1: 部分开票 2: 已开票 3: 无需开票)", example = "2")
    private Integer invoice;
    @ApiModelProperty(value = "核对状态 (0: 未核对 1: 已核对)", example = "1")
    private Integer check;
    @ApiModelProperty(value = "制单人",example = "王五")
    private String user;
}
