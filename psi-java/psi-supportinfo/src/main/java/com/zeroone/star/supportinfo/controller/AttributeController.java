package com.zeroone.star.supportinfo.controller;

import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.AddAttributeDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.AttributeDetailDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.AttributeDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.UpdateAttributeDTO;
import com.zeroone.star.project.j7.sysargs.supportinfo.supportattri.AttributeApis;
import com.zeroone.star.project.query.PageQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.supportattri.AttributeQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.supportinfo.service.IAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.zeroone.star.project.dto.PageDTO;

import javax.annotation.Resource;

/**
 * 类名：SupAttriController
 * 包名：com.zeroone.star
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@RestController
@RequestMapping("/attribute")
@Api(tags="辅助属性")
public class AttributeController implements AttributeApis {

    @Resource
    IAttributeService service;

    @PostMapping
    @ApiOperation("添加辅助属性")
    @Override
    public JsonVO<String> addSupportAttri(@Validated @RequestBody AddAttributeDTO addAttributeDTO) {
        String res=null;
        try {
            res=service.saveAttribute(addAttributeDTO);
        } catch (RuntimeException e) {
            return JsonVO.fail(e.getMessage());
        }
        return JsonVO.success(res);
    }

    @DeleteMapping
    @ApiOperation("删除辅助属性")
    @ApiImplicitParam(name = "id", value = "删除指定属性id", required = true, example = "1")
    @Override
    public JsonVO<String> removeSupportAttri(String id) {
        if (service.removeSupportAttri(id)) {
            return JsonVO.success(id);
        }
        return JsonVO.fail("删除失败");
    }

    @PutMapping
    @ApiOperation("修改辅助属性")
    @Override
    public JsonVO<String> modifySupportAttri(@Validated @RequestBody UpdateAttributeDTO updateAttributeDTO) {
            boolean res=false;
        try {
            res=service.updateSupportAttri(updateAttributeDTO);
        } catch (RuntimeException e) {
           return JsonVO.fail(e.getMessage());
        }
        return JsonVO.success(updateAttributeDTO.getId());
    }


    @GetMapping("/record")
    @ApiOperation("获取辅助属性列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<AttributeDTO>> querySupportAttri(@Validated AttributeQuery query) {
        return JsonVO.success(service.listSupportAttri(query));
    }
    @GetMapping("/get")
    @ApiOperation("获取指定属性详情")
    @ApiImplicitParam(name = "id", value = "属性ID", required = true, example = "1")
    @Override
    public JsonVO<AttributeDetailDTO> querySupportAttriDetail(String id) {
        return JsonVO.success(service.getSupportAttriDetail(id));
    }
    @GetMapping("/select")
    @ApiOperation("获取属性名与属性内容列表（分页）")
    @Override
    public JsonVO<PageDTO<AttributeDetailDTO>> querySupportAttriList(@Validated PageQuery query) {
        return JsonVO.success(service.listSupportAttriList(query));
    }
}
