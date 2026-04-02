package com.zeroone.star.project.query.j5.procurementreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


/**
 * 采购订单跟踪表查询类
 * @author yu
 * @date 2025/10/18
 */
@Data
@ApiModel("采购订单跟踪表查询参数")
public class PurchaseOrderTrackingFormQuery extends PageQuery {

    @ApiModelProperty(value = "供应商", example = "上海五金有限公司")
    private String supplier;
    @ApiModelProperty(value = "商品名称", example = "不锈钢螺丝")
    private String goods;
    @ApiModelProperty(value = "订单编号", example = "BOR-20231001-001")
    private String number;
    @ApiModelProperty(value = "入库状态", example = "0",allowableValues = "0,1,2,3")
    private Integer state;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "单据开始时间", example = "2023-10-01")
    private LocalDate begintime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "单据结束时间", example = "2023-10-05")
    private LocalDate endtime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "到货开始时间", example = "2023-10-01")
    private LocalDate beginarrival;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "到货结束时间", example = "2023-10-05")
    private LocalDate endarrival;
    @ApiModelProperty(value = "仓库", example = "上海主原料仓")
    private String warehouse;

    @NotNull(message = "排序方式(type)不能为空")
    @ApiModelProperty(value = "排序方式", example = "0", notes = "0=按单据时间排序, 1=按商品名称排序", required = true)
    private Integer type;
}
