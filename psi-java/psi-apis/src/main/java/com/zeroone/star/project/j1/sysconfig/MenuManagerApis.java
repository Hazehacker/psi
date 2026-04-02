package com.zeroone.star.project.j1.sysconfig;


import com.zeroone.star.project.dto.j1.sysconfig.MenuDeleteDTO;
import com.zeroone.star.project.dto.j1.sysconfig.MenuListDTO;
import com.zeroone.star.project.dto.j1.sysconfig.MenuUpdateDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j1.sysconfig.MenuListVO;
import com.zeroone.star.project.vo.j1.sysconfig.MenuNameTreeVO;

import java.util.List;

import com.zeroone.star.project.dto.j1.sysconfig.MenuAddDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j1.sysconfig.MenuDetailVO;

import javax.validation.Valid;


/**
 * <p>
 * 描述：菜单管理
 * </p>
 *
 * @author heavydrink
 * @version 1.0.0
 */
public interface MenuManagerApis {


    /**
     * 获取菜单名称树
     * @author ye
     * @return 菜单树结构
     */
    JsonVO<List<MenuNameTreeVO>> queryMenuNameTree();

    /**
     * 查询菜单列表
     * @return 菜单列表
     * @author ye
     */
    JsonVO<List<MenuListVO>> queryMenuList();
    //修改目录
    /*
     * 修改指定菜单
     * @param dto 修改对象
     * @return
     */

    JsonVO<String> modifyMenu(MenuUpdateDTO dto);

    /**
     * 删除指定菜单
     * @param id 删除对象
     * @return
     */
    JsonVO<String> deleteMenu(String id);

    /**
    * 新增菜单
    */
    JsonVO<String> addMenu(@Valid MenuAddDTO dto);


    /**
     * 获取指定菜单详情
    */
    JsonVO<MenuDetailVO> getMenuById(String id);


}
