package com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 描述：
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
@Data
@ApiModel("商品类别修改参数")
public class CategoryAddQuery {
    @ApiModelProperty(value = "父类别id", example = "0为默认类别", required = true)
    private String pid;

    /**
     * 类别名称
     */
    @ApiModelProperty(value = "类别名称", example = "生活用品", required = true)
    private String name;

    /**
     * 排序顺序
     */
    @ApiModelProperty(value = "排序顺序，小号在上", example = "0",required= true)
    private Integer sort;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "一个生活用品")
    private String data;
}
