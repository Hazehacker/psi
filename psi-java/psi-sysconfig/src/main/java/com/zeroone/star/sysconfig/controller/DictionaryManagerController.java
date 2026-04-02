package com.zeroone.star.sysconfig.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

//import cn.hutool.db.Page;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictAddDTO;

import com.zeroone.star.project.dto.j1.sysconfig.DictDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictNamesDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictUpdateDTO;
import com.zeroone.star.project.j1.sysconfig.DictionaryManagerApis;
import com.zeroone.star.project.query.j1.sysconfig.DictNameQuery;
import com.zeroone.star.project.query.j1.sysconfig.DictQuery;
import com.zeroone.star.project.query.j1.sysconfig.DictTypeNameQuery;
import com.zeroone.star.project.query.j1.sysconfig.DictTypeQury;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.sysconfig.entity.Dict;
import com.zeroone.star.sysconfig.entity.DictType;
import com.zeroone.star.sysconfig.service.IDictService;
import com.zeroone.star.sysconfig.service.IDictTypeService;
import com.zeroone.star.sysconfig.service.impl.DictServiceImpl;

import com.zeroone.star.sysconfig.service.IDictService;

import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.zeroone.star.project.dto.j1.sysconfig.DictTypeDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictTypeUpdateDTO;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping("/dictionary-manager")
@Api(tags="字典管理")
@Validated
public class DictionaryManagerController implements DictionaryManagerApis {

    @Resource
    DictServiceImpl dictServiceImpl;
    @Resource
    private IDictTypeService dictTypeService;

    @Resource
    IDictService dictService;

    @GetMapping("/dict-getNames")
    @ApiOperation(value = "获取字典名称列表")
    @Override
    public JsonVO<List<DictNamesDTO>> getDictNameList(@Validated DictNameQuery dictNameQuery) {
        return JsonVO.success(dictService.selectByTcode(dictNameQuery.getTypeCode()));
    }

    @GetMapping("/dict-queryList")
    @ApiOperation(value = "获取字典列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<DictDTO>> queryList(@Validated DictQuery condition) {
        return JsonVO.success(dictService.selectByCondition(condition));
    }

    /**
     * 新增字典类型
     * 创建一个新的字典类型，用于管理系统中的字典分类
     * @param dictTypeDTO 字典类型数据
     * @return 新增结果
     */
    @PostMapping("/add-dict-type")
    @ApiOperation(value = "新增字典类型", notes = "创建一个新的字典类型，用于管理系统中的字典分类")
    @Override
    public JsonVO<String> addDictType(@RequestBody  DictTypeDTO dictTypeDTO) {
        return dictTypeService.addDictType(dictTypeDTO);
    }

    /**
     *修改字典类型
     * @param updateDTO 字典类型修改数据
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改字典类型", notes = "根据ID修改字典类型的名称和备注信息（编码不可修改）")
    @Override
    public JsonVO<String> updateDictType(
            @PathVariable String id,
            @RequestBody DictTypeUpdateDTO updateDTO) {
        return dictTypeService.updateDictType(id, updateDTO);
    }

    /**
     * 删除字典类型
     * 根据ID删除字典类型，如果存在关联字典项
     * @param id 字典类型删除数据
     * @return
     */
    @DeleteMapping("/remove-dict-type{id}")
    @ApiOperation(value = "删除字典类型", notes = "根据ID删除字典类型，如果存在关联字典项删除失败")
    @Override
    public JsonVO<String> deleteDictType(@PathVariable String id) {

        return dictTypeService.deleteDictType(id);
    }

    @PostMapping
    @ApiOperation(value = "增加字典")
    @Override
    public JsonVO<String> addDict(@RequestBody DictAddDTO dictAddDTO) {

         String tid = dictAddDTO.getTid();
         String name = dictAddDTO.getName();
         String value = dictAddDTO.getValue();
        if (dictAddDTO == null) {
            return JsonVO.fail("新增失败，字典信息不能为空");
        }
        if (StringUtils.isEmpty(tid)) {
            return JsonVO.fail("新增失败，字典类型ID不能为空");
        }

        //  校验tid是否存在于字典类型表中
        DictType dictType = dictTypeService.getById(tid); // 调用字典类型Service查询
        if (dictType == null) {
            return JsonVO.fail("新增失败，字典类型ID不存在");
        }
        QueryWrapper<Dict> addQueryWrapper = new QueryWrapper<>();
        addQueryWrapper.eq("tid", tid)
                .eq("name", name)
                .or()
                .eq("value", value); // 同时校验三个条件
        long count = dictServiceImpl.count(addQueryWrapper);
        if (count > 0) {
            return JsonVO.fail("新增失败，该字典类型下已存在相同名称和值的字典");
        }
        // DTO转换为实体
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictAddDTO, dict);
            if (dictServiceImpl.save(dict)) {
                return JsonVO.success("新增成功");
            }
        return JsonVO.success("新增失败，未知错误");
    }

    @PutMapping
    @ApiOperation(value = "修改字典")
    @Override
    public JsonVO<String> updateDict(@RequestBody DictUpdateDTO dictUpdateDTO) {
        // 1. 基础参数校验
        if (dictUpdateDTO == null || dictUpdateDTO.getId() == null) {
            return JsonVO.fail("修改失败，字典ID不能为空");
        }
        String tid = dictUpdateDTO.getTid();
        String name = dictUpdateDTO.getName();
        String value = dictUpdateDTO.getValue();
        if (StringUtils.isEmpty(tid)) {
            return JsonVO.fail("修改失败，字典类型ID（tid）不能为空");
        }
        // 2. 校验字典是否存在（避免修改不存在的字典）
        Dict existingDict = dictServiceImpl.getById(dictUpdateDTO.getId());
        if (existingDict == null) {
            return JsonVO.fail("修改失败，字典ID=" + dictUpdateDTO.getId() + "不存在");
        }
        // 3. 校验tid是否存在于字典类型表
        DictType dictType = dictTypeService.getById(tid);
        if (dictType == null) {
            return JsonVO.fail("修改失败，字典类型ID（tid=" + tid + "）不存在");
        }
        QueryWrapper<Dict> addQueryWrapper = new QueryWrapper<>();
        addQueryWrapper.eq("tid", tid)
                .eq("name", name)
                .or()
                .eq("value", value); // 同时校验三个条件
        long count = dictServiceImpl.count(addQueryWrapper);
        if (count > 0) {
            return JsonVO.fail("新增失败，该字典类型下已存在相同名称和值的字典");
        }
        // 4. DTO转实体并更新
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictUpdateDTO, dict);
            if (dictServiceImpl.updateById(dict)) {
                return JsonVO.success("修改成功");
            } else {
                return JsonVO.fail("修改失败");
            }
        }

    @DeleteMapping("/{dictId}")
    @ApiOperation(value = "删除字典")
    @Override
    public JsonVO<String> deleteDict(@PathVariable("dictId") String dictId) {
        // 1. 校验ID非空
        if (dictId == null) {
            return JsonVO.fail("删除失败，字典ID无效");
        }
        // 2. 校验字典是否存在
        Dict existingDict = dictServiceImpl.getById(dictId);
        if (existingDict == null) {
            return JsonVO.fail("删除失败，字典ID=" + dictId + "不存在");
        }
        // 3. 执行删除操作
            if (dictServiceImpl.removeById(dictId)) {
                return JsonVO.success("删除成功");
            } else {
                return JsonVO.fail("删除失败，删除操作未生效");
            }

    }


    @Resource
    IDictTypeService iDictTypeService;
    @GetMapping("/dict-getNames-type")
    @Override
    @ApiOperation(value = "获取字典类型名称列表")
    public JsonVO<List<DictTypeNameQuery>> getDictTypeNameList() {
        List<DictTypeNameQuery> list =iDictTypeService.getDictTypeNameList();
        return JsonVO.success(list);
    }

    @GetMapping("/dict-queryList-type")
    @ApiOperation(value = "获取字典类型列表(条件+分页)")
    @Override
    public JsonVO<PageDTO<DictTypeDTO>> getDictTypeList( DictTypeQury dictTypeQury) {
        PageDTO<DictTypeDTO> list=   iDictTypeService.getDictTypeList(dictTypeQury);
        return JsonVO.success(list);
    }
}
