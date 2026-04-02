package com.zeroone.star.project.query.j5.procurementreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("采购汇总表查询参数")
public class PurchaseOrderSummaryFormQuery extends PageQuery {
    @ApiModelProperty(value = "商品名称", example = "小刀电动车")
    private String goods;
    @ApiModelProperty(value = "仓库信息", example = "上海总仓")
    private String warehouse;
    @ApiModelProperty(value = "供应商", example = "飞虎供应链有限公司")
    private String supplier;
    @ApiModelProperty(value = "用户", example = "张三")
    private String user;
    @ApiModelProperty(value = "时间范围-开始时间", example = "2025-10-01 00:00:00")
    private String startTime;
    @ApiModelProperty(value = "时间范围-结束时间", example = "2025-10-19 23:59:59")
    private String endTime;
    @ApiModelProperty(value = "关联人员", example = "李四")
    private String people;
    @ApiModelProperty(value = "汇总维度：goods-按商品, supplier-按供应商, people-按人员, user-按用户", example = "goods")
    private String summaryType;

}
