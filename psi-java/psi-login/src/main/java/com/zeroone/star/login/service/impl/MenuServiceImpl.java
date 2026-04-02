package com.zeroone.star.login.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeroone.star.login.entity.Menu;
import com.zeroone.star.login.mapper.MenuMapper;
import com.zeroone.star.login.service.IMenuService;
import com.zeroone.star.project.vo.login.Fun;
import com.zeroone.star.project.vo.login.MenuTreeVO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 * @author MRME39
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<MenuTreeVO> listMenuByRoleName() {
        //定义一个存储数据库查询菜单数据的容器
        List<MenuTreeVO> res = new ArrayList<>();
        //用于存储所有菜单节点，key为菜单ID
        Map<String, Menu> Menus = new HashMap<>();

        List<Menu>temp = baseMapper.selectAllMenu();
        for(Menu a : temp){
            Menus.put(a.getId(), a);
        }
        //构建菜单树
        return buildMenuTree(Menus).get(0).getChildren();
    }

    /**
     * 构建菜单，导入menue最后组装成MenuTreeVO
     */
    private List<MenuTreeVO> buildMenuTree(Map<String, Menu> Menus) {
        List<MenuTreeVO> result = new ArrayList<>();
        Map<String, MenuTreeVO> temp = new HashMap<>();
        //将menus转换成MenuTreeVO
        for(Map.Entry<String, Menu> menu : Menus.entrySet()){
            temp.put(menu.getKey(),toMenuTreeVO(menu.getValue()));
        }
        //找到默认菜单，根节点pid为-1
        for (MenuTreeVO menu : temp.values()) {
            //获取对应的VO的Menu中的pid
            if (isRootNode(Menus.get(menu.getId()).getPid())) {
                result.add(menu);
                break;
            }
        }
        Set<String> set = new HashSet<>();
        //父子关系搭建
        for (Map.Entry<String, MenuTreeVO> menu : temp.entrySet()) {
            if (!isRootNode(Menus.get(menu.getValue().getId()).getPid())
                    && !set.contains(menu.getKey())) {
                //查找父节点
                MenuTreeVO parent = temp.get(Menus.get(menu.getKey()).getPid());
                //父节点判断是否有附属界面,如果有就删除对应子界面并且修改reportHref和HasReport字段
                if(menu.getValue().getHasReport().equals("true")){
                    parent.setHasReport("true");
                    parent.setChildren(null);
                    parent.setReportHref(menu.getValue().getHref());
                }else{
                    parent.addChild(temp.get(menu.getKey()));
                }
                set.add(menu.getKey());
            }
        }

        // 对菜单树进行排序
        sortMenuTree(result, Menus);

        return result;
    }

    /**
     * 判断是否为根节点
     */
    private boolean isRootNode(String pid) {
        return "-1".equals(pid) || pid== null || pid.isEmpty();
    }

    /**
     * 通过角色名查询对应的界面id
     * @param root 角色名
     * @return 对应的root名
     */
    public List<String> analyroot(String root) {
        List<String> result = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Fun> funs = objectMapper.readValue(root,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Fun.class));
            for (Fun fun : funs) {
                result.add(fun.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将Menu类型转换成MenuTreeVO
     * @param m
     * @return
     */
    public MenuTreeVO toMenuTreeVO(Menu m){
        String Report = m.getHasReport();
        String a = Report.equals("0") ? "false" : "true";
        return new MenuTreeVO(m.getId(),m.getName(),m.getHref(),a);
    }

    /**
     * 递归对菜单树进行排序
     */
    private void sortMenuTree(List<MenuTreeVO> menuTree, Map<String, Menu> originalMenus) {
        if (menuTree == null || menuTree.isEmpty()) {
            return;
        }
        // 对当前层级菜单按 sort 排序
        menuTree.sort((m1, m2) -> {
            Menu menu1 = originalMenus.get(m1.getId());
            Menu menu2 = originalMenus.get(m2.getId());
            return menu1.getSort().compareTo(menu2.getSort());
        });
        // 递归对子菜单排序
        for (MenuTreeVO menu : menuTree) {
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                sortMenuTree(menu.getChildren(), originalMenus);
            }
        }
    }
}
