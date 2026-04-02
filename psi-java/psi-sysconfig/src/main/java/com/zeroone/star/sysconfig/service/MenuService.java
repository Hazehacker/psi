package com.zeroone.star.sysconfig.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j1.sysconfig.MenuAddDTO;
import com.zeroone.star.project.dto.j1.sysconfig.MenuDeleteDTO;
import com.zeroone.star.project.dto.j1.sysconfig.MenuUpdateDTO;

import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j1.sysconfig.MenuListVO;
import com.zeroone.star.project.vo.j1.sysconfig.MenuNameTreeVO;
import com.zeroone.star.sysconfig.entity.MenuDO;

import java.util.List;

import com.zeroone.star.project.vo.j1.sysconfig.MenuDetailVO;
import com.zeroone.star.sysconfig.entity.MenuDO;

import javax.validation.Valid;


/**
 * 菜单组件服务接口
 *
 * @author hui_cheng12
 * @version 1.0
 */
public interface MenuService extends IService<MenuDO> {

    /**
     * 获取菜单名称树
     *
     * @return
     * @author ye
     */
    JsonVO<List<MenuNameTreeVO>> queryMenuNameTree();

    /**
     * 获取菜单列表
     * @author ye
     */
    JsonVO<List<MenuListVO>> queryMenuList();

    /**
     * 修改目录组件
     * @param menuUpdateDTO
     * @returns
     */
    String updateMenu(MenuUpdateDTO menuUpdateDTO);

    /**
     * 删除目录组件
     * @param id
     * @return
     */
    String deleteMenu(String id);

    MenuDetailVO getMenuById(String id);

    String addMenu( MenuAddDTO dto);
}
