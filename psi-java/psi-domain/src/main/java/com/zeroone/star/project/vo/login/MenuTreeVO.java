package com.zeroone.star.project.vo.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import java.util.List;

/**
 * <p>
 * 描述：树状菜单显示数据
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author MRME39
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeVO{
    @ApiModelProperty(value = "序号", example = "001")
    private String id;
    @ApiModelProperty(value = "菜单名称", example = "主页")
    private String name;
    @ApiModelProperty(value = "路由路径", example = "/home")
    private String href;
    @ApiModelProperty(value = "菜单类型", example = "false")
    private String hasReport;
    @ApiModelProperty(value = "报表页面的路径", example = "/purchase-booking-report")
    private String reportHref;
    @ApiModelProperty(value = "节点包含的子节点")
    private List<MenuTreeVO> children;

    public MenuTreeVO(String id, String name, String href, String hasReport) {
        this.id = id;
        this.name = name;
        this.href = href;
        this.hasReport = hasReport;
        this.children = new ArrayList<MenuTreeVO>();
    }

    public void addChild(MenuTreeVO m){
        this.children.add(m);
    }
}
