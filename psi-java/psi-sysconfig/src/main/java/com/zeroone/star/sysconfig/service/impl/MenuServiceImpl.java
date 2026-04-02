package com.zeroone.star.sysconfig.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j1.sysconfig.MenuAddDTO;

import com.zeroone.star.project.dto.j1.sysconfig.MenuUpdateDTO;

import com.zeroone.star.project.utils.tree.TreeNode;
import com.zeroone.star.project.utils.tree.TreeNodeMapper;
import com.zeroone.star.project.utils.tree.TreeUtils;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j1.sysconfig.MenuListVO;
import com.zeroone.star.project.vo.j1.sysconfig.MenuNameTreeVO;
import com.zeroone.star.sysconfig.entity.MenuDO;
import com.zeroone.star.sysconfig.mapper.MenuMapper;
import com.zeroone.star.sysconfig.service.MenuService;
import lombok.extern.slf4j.Slf4j;

import com.zeroone.star.project.vo.j1.sysconfig.MenuDetailVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDO> implements MenuService {


    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MsMenuMapper msMenuMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 内部适配器类：将 MenuNameTreeVO 适配为 TreeNode
     * @author ye
     */
    private static class MenuTreeNodeAdapter extends TreeNode {
        private final MenuNameTreeVO originalVO;

        public MenuTreeNodeAdapter(MenuNameTreeVO vo) {
            this.originalVO = vo;
            // 设置 TreeNode 需要的字段
            this.setTnId(String.valueOf(vo.getId()));
            this.setTnPid(vo.getPid() == null || vo.getPid() == "0" ?
                    null : String.valueOf(vo.getPid()));
        }

        /**
         * 将 TreeNode 结构转换回 MenuNameTreeVO 结构
         * @author ye
         */
        public MenuNameTreeVO convertToMenuVO() {
            MenuNameTreeVO result = new MenuNameTreeVO();
            result.setId(originalVO.getId());
            result.setName(originalVO.getName());
            result.setPid(originalVO.getPid());
            result.setSort(originalVO.getSort());

            // 递归处理子节点
            if (this.tnChildren != null && !this.tnChildren.isEmpty()) {
                List<MenuNameTreeVO> children = this.tnChildren.stream()
                        .map(child -> ((MenuTreeNodeAdapter) child).convertToMenuVO())
                        .collect(Collectors.toList());
                result.setChildren(children);
            }

            return result;
        }
    }

    /**
     * 获取菜单名称树
     *
     * @return
     * @author ye
     */
    @Override
    public JsonVO<List<MenuNameTreeVO>> queryMenuNameTree() {
        try{
            //1.在Service层执行查询和转换
            List<MenuNameTreeVO> allMenus= getAllMenusForTreeOptimized();

            if(CollectionUtils.isEmpty(allMenus)){
                log.error("未找到菜单数据");
                return JsonVO.success(new ArrayList<>());
            }
            //2.使用项目工具类构建树型结构
            List<MenuNameTreeVO> menuTree=buildMenuTreeWithUtils(allMenus);
            //3.对树排序
            sortMenuTree(menuTree);
            return JsonVO.success(menuTree);

        }catch (Exception e){
            log.error("获取菜单名称树失败",e);
            return JsonVO.fail(new ArrayList<>());
        }


    }



    /**
     * 获取所有菜单数据并转换为VO
     * @author ye
     * @return
     */
    private List<MenuNameTreeVO> getAllMenusForTreeOptimized()  {
        List<MenuDO> menuList = lambdaQuery()
                .select(MenuDO::getId, MenuDO::getName, MenuDO::getPid, MenuDO::getSort)
                .orderByAsc(MenuDO::getSort)
                .list();

        // 数据转换
        return menuList.stream()
                .map(this::convertToTreeVO)
                .collect(Collectors.toList());
    }

    /**
     * MenuDO 转 MenuNameTreeVO 的方法
     * @author ye
     */
    private MenuNameTreeVO convertToTreeVO(MenuDO menuDO) {
        MenuNameTreeVO vo = new MenuNameTreeVO();
        vo.setId(menuDO.getId());
        vo.setName(menuDO.getName());
        vo.setPid(menuDO.getPid());
        vo.setSort(menuDO.getSort());
        vo.setTnId(String.valueOf(menuDO.getId()));
        vo.setTnPid(menuDO.getPid() == null || menuDO.getPid() == "0" ?
                null : String.valueOf(menuDO.getPid()));
        return vo;
    }


    /**
     * 使用项目工具类构建树形结构
     * @author ye
     */
    private List<MenuNameTreeVO> buildMenuTreeWithUtils(List<MenuNameTreeVO> menus) {
        // 1. 创建原始数据的映射，用于后续转换
        Map<String, MenuNameTreeVO> originalMap = menus.stream()
                .collect(Collectors.toMap(
                        vo -> String.valueOf(vo.getId()),
                        vo -> vo
                ));

        // 2. 将 MenuNameTreeVO 转换为 MenuTreeNodeAdapter
        List<MenuTreeNodeAdapter> adapters = menus.stream()
                .map(MenuTreeNodeAdapter::new)
                .collect(Collectors.toList());

        // 3. 使用 TreeUtils 构建树
        List<MenuTreeNodeAdapter> treeResult = TreeUtils.listToTree(adapters,
                new TreeNodeMapper<MenuTreeNodeAdapter>() {
                    @Override
                    public TreeNode objectMapper(MenuTreeNodeAdapter adapter) {
                        return adapter;
                    }
                });

        // 4. 将适配器转换回 MenuNameTreeVO
        return treeResult.stream()
                .map(MenuTreeNodeAdapter::convertToMenuVO)
                .collect(Collectors.toList());
    }
    /**
     * 递归排序菜单树
     * @author ye
     */
    private void sortMenuTree(List<MenuNameTreeVO> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }

        menus.sort(Comparator.comparing(MenuNameTreeVO::getSort,
                Comparator.nullsFirst(Comparator.naturalOrder())));

        for (MenuNameTreeVO menu : menus) {
            List<MenuNameTreeVO> children = menu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sortMenuTree(children);
            }
        }
    }

    /**
     * 获取菜单列表
     * @author ye
     */
    @Override
    public JsonVO<List<MenuListVO>> queryMenuList() {
        try{
            //1.获取所有菜单数据
            List<MenuDO> menuDOList = getAllMenusForList();

            if(CollectionUtils.isEmpty(menuDOList)){
                log.info("未找到菜单数据");
                return JsonVO.success(new ArrayList<>());
            }
            //2.转换为MenuListVO列表
            List<MenuListVO> menuVOList=convertToMenuListVO(menuDOList);

            //3.构建树型结构
            List<MenuListVO> menuTree=buildMenuListTree(menuVOList);

            //4.对树进行排序
            sortMenuListTree(menuTree);

            return JsonVO.success(menuTree);
        }catch (Exception e){
            log.error("获取菜单列表失败",e);
            return JsonVO.fail(new ArrayList<>());
        }
    }
    /**
     * 获取所有菜单数据（用于菜单列表，查询所有字段）
     * @author ye
     * @return 菜单DO列表
     */
    private List<MenuDO> getAllMenusForList() {

        List<MenuDO> menuList = lambdaQuery()
                .orderByAsc(MenuDO::getSort)
                .list();

        log.info("从数据库查询到 {} 条菜单数据", menuList.size());
        return menuList;
    }

    /**
     * 将 MenuDO 转换为 MenuListVO
     * @author ye
     * @param menuDOList 菜单DO列表
     * @return 菜单VO列表
     */
    private List<MenuListVO> convertToMenuListVO(List<MenuDO> menuDOList) {
        return menuDOList.stream()
                .map(menuDO -> {
                    MenuListVO vo = new MenuListVO();
                    // 复制相同字段
                    vo.setId(menuDO.getId()); // Integer 转 Long
                    vo.setName(menuDO.getName());
                    vo.setKey(menuDO.getKey());
                    vo.setModel(menuDO.getModel());
                    vo.setType(menuDO.getType());
                    vo.setResource(menuDO.getResource());
                    vo.setSort(menuDO.getSort());
                    vo.setData(menuDO.getData());
                    // 处理父级ID转换
                    vo.setPid(menuDO.getPid() != null ? menuDO.getPid() : "0");
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 构建菜单列表树形结构
     * @author ye
     * @param allMenus 所有菜单VO列表
     * @return 树形结构的菜单列表
     */
    private List<MenuListVO> buildMenuListTree(List<MenuListVO> allMenus) {
        // 创建根节点列表
        List<MenuListVO> rootMenus = new ArrayList<>();

        // 创建ID到菜单的映射
        Map<String, MenuListVO> menuMap = new HashMap<>();

        // 第一次遍历：建立映射
        for (MenuListVO menu : allMenus) {
            menuMap.put(menu.getId(), menu);
        }

        // 第二次遍历：建立父子关系
        for (MenuListVO menu : allMenus) {
            String pid = menu.getPid();

            if (pid == null || pid == "0") {
                // 根节点
                rootMenus.add(menu);
            } else {
                // 子节点，找到父节点
                MenuListVO parent = menuMap.get(pid);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        }

        log.info("菜单列表树构建完成，共 {} 个根节点", rootMenus.size());
        return rootMenus;
    }

    /**
     * 递归排序菜单列表树
     * @author ye
     * @param menus 菜单列表树
     */
    private void sortMenuListTree(List<MenuListVO> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }

        // 对当前层级排序
        menus.sort(Comparator.comparing(MenuListVO::getSort,
                Comparator.nullsFirst(Comparator.naturalOrder())));

        // 递归排序子节点
        for (MenuListVO menu : menus) {
            List<MenuListVO> children = menu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sortMenuListTree(children);
            }
        }
    }





    @Override
    public String updateMenu(MenuUpdateDTO menuUpdateDTO) {

        // 执行更新
        MenuDO menuDO = msMenuMapper.DTO_DO(menuUpdateDTO);

        int updated = menuMapper.update(menuDO,
                Wrappers.<MenuDO>lambdaUpdate()
                        .eq(MenuDO::getId, menuUpdateDTO.getId()));

        if (updated <= 0) {
            return "修改失败";
        }

        return menuUpdateDTO.getId().toString();
    }

    /**
     * 删除指定菜单(递归去删)
     * @param id
     * @return
     */
    @Override
    public String deleteMenu(String id) {
        // 1. 获取要删除的本节点ID（目标节点）

        // 2. 递归查询所有子节点ID（以targetId为父id的所有子节点）
        List<String> allChildIds = menuMapper.selectAllChildIds(id);

        // 3. 关键：将本节点ID加入待删除集合（一起删除）
        List<String> idsToDelete = new ArrayList<>();
        idsToDelete.add(id); // 加入本节点
        if (allChildIds != null && !allChildIds.isEmpty()) {
            idsToDelete.addAll(allChildIds); // 加入所有子节点
        }
        menuMapper.setRecursionDepth();
        // 4. 批量删除本节点 + 所有子节点
        int deleted = menuMapper.deleteBatchIds(idsToDelete);

        // 5. 返回结果
        if (deleted > 0) {
            return String.valueOf(deleted);
        } else {
            return "0";
        }
    }

    @Override
    public MenuDetailVO getMenuById(String id) {
        String key = "menu:" + id;
        MenuDetailVO menu = (MenuDetailVO) redisTemplate.opsForValue().get(key);
        if (menu != null) {
            return menu; // 缓存命中
        }
        MenuDO menuDO = menuMapper.selectById(id);
        if (menuDO != null){
            redisTemplate.opsForValue().set(key,msMenuMapper.MenuToDetailVO(menuDO));
        }
        return msMenuMapper.MenuToDetailVO(menuDO);


    }

    @Override
    public String addMenu(MenuAddDTO dto) {
        MenuDO menuDO = msMenuMapper.AddDtoToMenu(dto);
        boolean saved = this.save(menuDO);
        if (!saved) {
            return "FAIL";
        }
        Set<String> keys = redisTemplate.keys("menu:*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
        return menuDO.getId(); // 返回新增菜单id
    }
}
