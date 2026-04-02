package com.zeroone.star.supportinfo.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.MenuDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.SaveOftenReqDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.SaveOftenRespDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.often.UserOftenDTO;
import com.zeroone.star.project.j7.sysargs.supportinfo.often.OftenApis;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.often.AvailableOftenQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.often.UserOftenQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.supportinfo.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
/**
 * 类名：OftenController
 * 包名：com.zeroone.star
 * 描述：
 * 作者：不易
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@RestController
@RequestMapping("/often")
@Api(tags = "常用功能")
public class OftenController implements OftenApis {
    @Resource
    private IMenuService menuService;

    @Override
    @GetMapping("/available")
    @ApiOperation("获取可设置的常用功能")
    public JsonVO<List<MenuDTO>> listAvailableOftenMenus(@Validated AvailableOftenQuery query) {
        List<MenuDTO> list = menuService.getAvailableOftenMenus(query.getUserId());
        return JsonVO.success(list);
    }

    @Override
    @GetMapping("/configured")
    @ApiOperation("获取已设置的常用功能")
    public JsonVO<PageDTO<UserOftenDTO>> getConfiguredOftenMenus(@Validated UserOftenQuery query) {
        PageDTO<UserOftenDTO> result = menuService.getConfiguredOftenMenus(query);
        return JsonVO.success(result);
    }

    @Override
    @PostMapping("/save")
    @ApiOperation("保存设置")
    public JsonVO<SaveOftenRespDTO> saveOftenMenus(@Validated @RequestBody SaveOftenReqDTO reqDTO) {
        SaveOftenRespDTO resp = menuService.saveUserOftenMenus(reqDTO);
        return JsonVO.success(resp);
    }
}
