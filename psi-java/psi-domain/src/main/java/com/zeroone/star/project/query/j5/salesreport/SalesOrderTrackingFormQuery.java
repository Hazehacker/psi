package com.zeroone.star.project.query.j5.salesreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 销售订单跟踪表查询类
 *
 * @author leyu
 * @date 2025-10-19
 */
@Data
@ApiModel("销售订单跟踪表查询参数")
public class SalesOrderTrackingFormQuery extends PageQuery {

    @ApiModelProperty(value = "客户", example = "A公司")
    private String customer;
    @ApiModelProperty(value = "商品名称", example = "衬衫")
    private String goods;
    @ApiModelProperty(value = "订单编号", example = "1000001")
    private String number;
    @ApiModelProperty(value = "出库状态", example = "0",allowableValues = "0,1,2,3")
    private Integer state;
    @ApiModelProperty(value = "单据开始时间", example = "2025-10-01")
    private LocalDate begintime;
    @ApiModelProperty(value = "单据结束时间", example = "2025-10-31")
    private LocalDate endtime;
    @ApiModelProperty(value = "到货开始时间", example = "2025-10-01")
    private LocalDate beginarrival;
    @ApiModelProperty(value = "到货结束时间", example = "2025-10-31")
    private LocalDate endarrival;
    @ApiModelProperty(value = "仓库", example = "1号仓库")
    private String warehouse;

    @NotNull(message = "排序方式(type)不能为空")
    @ApiModelProperty(value = "排序方式", example = "0", notes = "0=按单据时间排序, 1=按商品名称排序", required = true)
    private Integer type;
}
