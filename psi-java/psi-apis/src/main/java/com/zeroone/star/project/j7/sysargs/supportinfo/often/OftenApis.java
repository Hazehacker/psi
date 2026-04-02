package com.zeroone.star.project.j7.sysargs.supportinfo.often;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.MenuDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.SaveOftenReqDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.SaveOftenRespDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.UserOftenDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.often.AvailableOftenQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.often.UserOftenQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.Api;

import java.util.List;

/**
 * 类名：OftenApis
 * 包名：com.zeroone.star.project.j7.sysargs.supportinfo.often
 * 描述：常用功能接口
 * 作者：不易
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
public interface OftenApis {
    /**
     * 获取可设置的常用功能
     * 根据用户ID返回其有权限访问的、可设置为“常用功能”的菜单
     * @param query
     * @return 可设置的常用功能列表
     */
    JsonVO<List<MenuDTO>> listAvailableOftenMenus(AvailableOftenQuery query);
    /**
     * 获取用户已设置的常用功能
     * @param query 用户 ID 查询参数（包含 userId）
     * @return 用户已设置的常用功能对象
     */
    JsonVO<PageDTO<UserOftenDTO>> getConfiguredOftenMenus(UserOftenQuery query);
    /**
     * 保存用户常用菜单配置
     * @param reqDTO 待保存的用户 ID 和菜单 ID 列表
     * @return 保存结果响应
     */
    JsonVO<SaveOftenRespDTO> saveOftenMenus(SaveOftenReqDTO reqDTO);
}
