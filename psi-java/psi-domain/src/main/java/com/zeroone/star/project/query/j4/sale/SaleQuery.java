package com.zeroone.star.project.query.j4.sale;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 描述：销售单查询参数
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售单查询参数")
public class SaleQuery extends PageQuery {

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "单据编号")
    private String number;

    @ApiModelProperty(value = "客户")
    private String customer;

    @ApiModelProperty(value = "关联人员")
    private String people;

    @ApiModelProperty(value = "制单人")
    private String user;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]")
    private Integer examine;

    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]")
    private Integer nucleus;

    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]")
    private Integer cse;

    @ApiModelProperty(value = "发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]")
    private Integer invoice;

    @ApiModelProperty(value = "核对状态[0:未核对|1:已核对]")
    private Integer check;

    @ApiModelProperty(value = "备注信息")
    private String data;
}