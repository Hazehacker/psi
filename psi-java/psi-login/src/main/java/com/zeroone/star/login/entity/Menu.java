package com.zeroone.star.login.entity;

import com.zeroone.star.project.vo.login.Fun;
import com.zeroone.star.project.vo.login.MenuTreeVO;
import lombok.Getter;
import lombok.Setter;

import javax.swing.plaf.ProgressBarUI;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单
 * </p>
 * @author 阿伟
 */
@Getter
@Setter
public class Menu implements Serializable {

    /**
     * 菜单编号
     */
    private String id;
    /**
     * 所属菜单id(父节点)
     */
    private String pid;
    /**
     * 菜单名
     */
    private String name;
    /**
     * 路由路劲
     */
    private String href;
    /**
     * 菜单排序
     */
    private Integer sort;
    /**
     * 菜单类型
     */
    private String hasReport;
    /**
     * 子菜单
     */
    private List<MenuTreeVO> children = new ArrayList<>();

}
