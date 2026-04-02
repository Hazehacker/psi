package com.zeroone.star.supportinfo.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.MenuDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.SaveOftenReqDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.SaveOftenRespDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.UserOftenDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.often.UserOftenQuery;
import com.zeroone.star.supportinfo.entity.Menu;
import com.zeroone.star.supportinfo.entity.Often;
import com.zeroone.star.supportinfo.mapper.MenuMapper;
import com.zeroone.star.supportinfo.mapper.OftenMapper;
import com.zeroone.star.supportinfo.service.IMenuService;
import com.zeroone.star.supportinfo.service.IOftenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单信息 服务实现类
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private OftenMapper oftenMapper;

    @Resource
    private IOftenService oftenService;
    /**
     * 获取用户可用的常用功能菜单
     *
     * @param userId 用户ID
     * @return 可用的菜单列表
     */
    /**
     * 获取用户可用的常用功能菜单
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getAvailableOftenMenus(String userId) {
        // 获取用户已配置的 key
        List<String> configuredKeys = oftenMapper.selectConfiguredKeys(userId);

        // 查询所有有 key 的菜单
        LambdaQueryWrapper<Menu> query = new LambdaQueryWrapper<>();
        query.isNotNull(Menu::getKey).ne(Menu::getKey, "");
        List<Menu> allMenus = this.list(query);

        return allMenus.stream()
                .filter(menu -> !configuredKeys.contains(menu.getKey()))
                .map(this::convertToMenuDTO)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户已配置的常用功能菜单（分页）
     */
    @Override
    @Transactional(readOnly = true)
    public PageDTO<UserOftenDTO> getConfiguredOftenMenus(UserOftenQuery query) {
        String userId = query.getUserId();
        Page<Often> page = new Page<>(query.getPageIndex(), query.getPageSize());

        LambdaQueryWrapper<Often> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Often::getUser, userId);

        Page<Often> oftenPage = oftenMapper.selectPage(page, wrapper);
        return PageDTO.create(oftenPage, this::convertOftenToUserOftenDTO);
    }

    /**
     * 将 Often 实体转换为 UserOftenDTO
     */
    private UserOftenDTO convertOftenToUserOftenDTO(Often often) {
        UserOftenDTO dto = new UserOftenDTO();
        dto.setUserId(often.getUser());

        LambdaQueryWrapper<Menu> query = new LambdaQueryWrapper<>();
        query.eq(Menu::getKey, often.getKey());
        Menu menu = this.getOne(query);

        MenuDTO menuDTO = menu != null
                ? convertToMenuDTO(menu)
                : buildFallbackMenuDTO(often);

        dto.setMenus(Collections.singletonList(menuDTO));
        return dto;
    }

    /**
     * 构建菜单不存在时的兜底 MenuDTO（保留名称和 key，root 设为空）
     */
    private MenuDTO buildFallbackMenuDTO(Often often) {
        MenuDTO dto = new MenuDTO();
        dto.setId("");
        dto.setName(often.getName());
        dto.setKey(often.getKey());
        dto.setResource("");
        dto.setIco("");
        dto.setRoot(""); // 菜单已删除，无法获取 root
        return dto;
    }

    /**
     * 将 Menu 实体转换为 MenuDTO
     */
    private MenuDTO convertToMenuDTO(Menu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setName(menu.getName());
        dto.setKey(menu.getKey());
        dto.setResource(menu.getResource());
        dto.setIco(menu.getIco());
        dto.setRoot(menu.getRoot());
        return dto;
    }

    /**
     * 保存用户常用功能菜单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveOftenRespDTO saveUserOftenMenus(SaveOftenReqDTO reqDTO) {
        String userId = reqDTO.getUserId();
        List<String> keys = reqDTO.getMenuKeys();

        // 清空旧数据
        oftenMapper.deleteByUser(userId);

        if (keys == null || keys.isEmpty()) {
            SaveOftenRespDTO resp = new SaveOftenRespDTO();
            resp.setSuccess(true);
            resp.setMessage("保存成功!");
            return resp;
        }

        // 查询菜单
        LambdaQueryWrapper<Menu> menuQuery = new LambdaQueryWrapper<>();
        menuQuery.in(Menu::getKey, keys);
        List<Menu> menus = this.list(menuQuery);

        if (menus.isEmpty()) {
            SaveOftenRespDTO resp = new SaveOftenRespDTO();
            resp.setSuccess(false);
            resp.setMessage("未找到任何有效菜单");
            return resp;
        }

        // 构建 Often 实体
        List<Often> entities = menus.stream().map(menu -> {
            Often often = new Often();
            often.setId(UUID.randomUUID().toString().replace("-", ""));
            often.setUser(userId);
            often.setName(menu.getName());
            often.setKey(menu.getKey());
            return often;
        }).collect(Collectors.toList());

        // 批量插入
        try {
            if (oftenService.saveBatch(entities)) {
                SaveOftenRespDTO resp = new SaveOftenRespDTO();
                resp.setSuccess(true);
                resp.setMessage("保存成功!");
                return resp;
            } else {
                throw new RuntimeException("批量保存失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("保存失败: " + e.getMessage(), e);
        }
    }
}
