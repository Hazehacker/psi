package com.zeroone.star.project.query.j5.procurementreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 采购排行表查询类
 * @author impovo123
 * @date 2025/10/20
 */
@Data
@ApiModel(value = "采购排行表查询参数")
public class PurchaseRankingFormQuery extends PageQuery {

    @ApiModelProperty(value = "商品名称", example = "联想笔记本电脑")
    private String goodsName;

    @ApiModelProperty(value = "商品编号", example = "lenovo123")
    private String goodsNumber;

    @ApiModelProperty(value = "单据开始日期", example = "2025-10-01")
    private String beginTime;

    @ApiModelProperty(value = "单据结束日期", example = "2025-10-31")
    private String endTime;

}
