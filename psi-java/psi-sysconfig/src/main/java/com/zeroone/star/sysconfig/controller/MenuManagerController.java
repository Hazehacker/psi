package com.zeroone.star.sysconfig.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.project.dto.j1.sysconfig.MenuDeleteDTO;
import com.zeroone.star.project.dto.j1.sysconfig.MenuListDTO;
import com.zeroone.star.project.dto.j1.sysconfig.MenuUpdateDTO;
import com.zeroone.star.project.j1.sysconfig.MenuManagerApis;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.project.vo.ResultStatus;
import com.zeroone.star.project.vo.j1.sysconfig.MenuListVO;
import com.zeroone.star.project.vo.j1.sysconfig.MenuNameTreeVO;

import com.zeroone.star.sysconfig.entity.MenuDO;
import com.zeroone.star.sysconfig.service.MenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.zeroone.star.project.dto.j1.sysconfig.MenuAddDTO;
import com.zeroone.star.project.j1.sysconfig.MenuManagerApis;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j1.sysconfig.MenuDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/menu-manager")
@Api(tags="菜单管理")
@Validated
public class MenuManagerController implements MenuManagerApis {

    @Autowired
    private MenuService menuService;
    /**
     * 获取菜单树
     * @author ye
     * @return
     */
    @Override
    @GetMapping("/query-menuTree")
    @ApiOperation(value = "获取菜单名称树")
    public JsonVO<List<MenuNameTreeVO>> queryMenuNameTree() {
        return menuService.queryMenuNameTree();
    }


    /**
     * 获取菜单列表
     * @author  ye
     * @param
     * @return
     */
    @Override
    @GetMapping("/query-menuList")
    @ApiOperation(value = "获取菜单列表")
    public JsonVO<List<MenuListVO>> queryMenuList() {

        return menuService.queryMenuList();
    }


    /**
     * 修改指定菜单
     * @param dto 修改对象
     * @return
     */
    @PutMapping("/modify-menu/{id}")
    @ApiOperation(value = "修改指定菜单")
    @Override
    public JsonVO<String> modifyMenu(@RequestBody MenuUpdateDTO dto) {
        String s = menuService.updateMenu(dto);
        if (s != null && s.matches("\\d+")) {
            return JsonVO.success(s);
        }
        String errorMsg=(s == null || s.isEmpty())?"操作失败" : s;
        return JsonVO.fail(errorMsg);
    }

    /**
     * 新增菜单
     * @param dto 新增对象
     * @return 响应结果
     */
    @PostMapping("/add-menu")
    @ApiOperation(value = "新增菜单")
    @Override
    public JsonVO<String> addMenu(@Valid @RequestBody MenuAddDTO dto) {
        MenuDO one = menuService.getOne(new QueryWrapper<MenuDO>().lambda().eq(MenuDO::getKey, dto.getKey()));
        if (one != null) {
            return JsonVO.fail("菜单标识已存在");
        }
        String result = menuService.addMenu(dto);
        if (!"FAIL".equals(result)) {
            return JsonVO.success(result);//返回新增菜单id
        }
        return JsonVO.fail("新增菜单失败");
    }

    /**

     * 删除指定菜单
     * @param id 删除对象
     * @return
     */
    @DeleteMapping("/delete-menu/{id}")
    @ApiOperation(value = "删除指定菜单")
    @Override
    public JsonVO<String> deleteMenu(@PathVariable("id") String id) {
        // 检查 id 是否为空
        if (id == null || id.isEmpty()) {
            return JsonVO.fail("删除id不存在");
        }

        // 调用服务层删除菜单
        String result = menuService.deleteMenu(id);

        // 判断是否删除成功
        if (result != null && result.matches("\\d+") && !"0".equals(result)) {
            return JsonVO.success("删除成功");
        } else {
            // 返回具体的错误信息
            String errorMsg = (result != null && !result.isEmpty()) ? result : "操作失败";
            return JsonVO.fail(errorMsg);
        } }


    /*
     * 获取指定菜单详情
     * @param id 菜单id
     * @return 菜单详情
     */
    @GetMapping("/get-by-id/{id}")
    @ApiOperation(value = "获取指定菜单详情")
    @Override
    public JsonVO<MenuDetailVO> getMenuById(@PathVariable String id) {

        MenuDetailVO vo = menuService.getMenuById(id);
        if (vo == null) {
            return JsonVO.create(null, ResultStatus.FAIL.getCode(), "未找到对应菜单");
        }
        return JsonVO.success(vo);
    }
}
