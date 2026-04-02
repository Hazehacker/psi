package com.zeroone.star.supportinfo.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.MenuDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.SaveOftenReqDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.SaveOftenRespDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.UserOftenDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.often.UserOftenQuery;
import com.zeroone.star.supportinfo.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单信息 服务类
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
public interface IMenuService extends IService<Menu> {
    List<MenuDTO> getAvailableOftenMenus(String userId);
    PageDTO<UserOftenDTO> getConfiguredOftenMenus(UserOftenQuery query);
    SaveOftenRespDTO saveUserOftenMenus(SaveOftenReqDTO reqDTO);
}
