package com.zeroone.star.project.query.j1.sysconfig;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 描述：字典类型名称查询数据对象
 * </p>
 *
 * @Author: ZGjie20
 * @version: 1.0.0
 */
@Data
@ApiModel("字典类型名称查询对象")

public class DictTypeNameQuery  {
    @ApiModelProperty(value = "字典类型名称", example = "fastdfs")
 String name;
    @ApiModelProperty(value = "字典类型id", example = "61f3bbc90b904b51890448968afc106f")
    String id;
}
