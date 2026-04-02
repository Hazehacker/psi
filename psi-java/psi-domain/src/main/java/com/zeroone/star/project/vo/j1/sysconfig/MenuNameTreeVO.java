package com.zeroone.star.project.vo.j1.sysconfig;

import com.zeroone.star.project.utils.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
/**
 * <p>
 * 描述：菜单名称树对象
 * </p>
 * @author ye
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("菜单名称树对象")
public class MenuNameTreeVO extends TreeNode {
    @ApiModelProperty(value = "菜单id", example = "1")
    private String id;
    @ApiModelProperty(value = "菜单名称", example = "首页")
    private String name;

    @ApiModelProperty(value = "父级菜单id", example = "0")
    private String pid;
    @ApiModelProperty(value="菜单排序", example = "1")
    private Integer sort;



    @ApiModelProperty(value = "节点包含的子节点")
    public List<MenuNameTreeVO> children;

}
