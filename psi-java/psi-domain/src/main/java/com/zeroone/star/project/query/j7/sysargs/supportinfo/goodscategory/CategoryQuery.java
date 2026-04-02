package com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("根据商品类别名模糊查询")
public class CategoryQuery extends PageQuery {
    @ApiModelProperty(value = "商品类别名称", example = "鞋")
    private String name;
}
