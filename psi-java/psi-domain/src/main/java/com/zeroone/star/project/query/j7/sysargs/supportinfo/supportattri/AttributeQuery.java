package com.zeroone.star.project.query.j7.sysargs.supportinfo.supportattri;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 描述：辅助属性查询对象
 * </p>
 * @author yi
 * @version 1.0
 */
@Data
@ApiModel("辅助属性查询参数")
public class AttributeQuery extends PageQuery {
    @ApiModelProperty(value = "属性", example = "颜色")
    private String name;
    @ApiModelProperty(value = "备注属性", example = "服饰颜色")
    private String data;
}