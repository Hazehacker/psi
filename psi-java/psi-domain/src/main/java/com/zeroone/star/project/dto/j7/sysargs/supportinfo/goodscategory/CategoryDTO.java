package com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

/**
 * <p>
 * 描述：单个商品类别数据
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 圣魔
 * @version 1.0.0
 */
@ApiModel("商品类型对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {


    /**
     * 本类别id
     */
    @NotBlank(message = "当前类别id不能为空")
    @Pattern(regexp = "^\\d+$", message = "当前类别id必须为非负整数")
    @ApiModelProperty(value = "本类别id", example = "1", required = true)
    private String id;
    /**
     * 父类别id
     */
    @NotBlank(message = "父类别id不能为空")
    @Pattern(regexp = "^\\d+$", message = "父类别id必须为非负整数")
    @ApiModelProperty(value = "父类别id（0为默认类别）", example = "0", required = true)
    private String pid;

    /**
     * 类别名称
     */
    @NotBlank(message = "名称不能为空")
    @ApiModelProperty(value = "类别名称", example = "生活用品", required = true)
    private String name;

    /**
     * 排序顺序
     */
    @NotNull(message = "排序值不能为空")
    @PositiveOrZero(message = "排序值必须为非负整数")
    @ApiModelProperty(value = "排序顺序,小号在上",example = "0",required = true)
    private Integer sort;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "一个生活用品")
    private String data;

}
