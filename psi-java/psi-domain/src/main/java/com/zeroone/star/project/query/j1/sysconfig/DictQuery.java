package com.zeroone.star.project.query.j1.sysconfig;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 描述：字典查询数据对象
 * </p>
 * @author fish
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("字典查询对象")
public class DictQuery extends PageQuery {


    @ApiModelProperty(value = "字典类型唯一标识", example = "f42afd7d846248528fe72d62c01e9d1e")
    String tid;

    @ApiModelProperty(value = "字典名称", example = "fastdfs")
    String name;

    @ApiModelProperty(value = "字典值", example = "10001")
    String value;

}
