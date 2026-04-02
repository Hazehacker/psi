package com.zeroone.star.sysconfig.service.impl;

import cn.hutool.core.bean.BeanUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.project.dto.j1.sysconfig.DictTypeDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictTypeDeleteDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictTypeUpdateDTO;
import com.zeroone.star.project.vo.JsonVO;

//import com.alibaba.spring.util.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictTypeDTO;
import com.zeroone.star.project.query.j1.sysconfig.DictTypeNameQuery;
import com.zeroone.star.project.query.j1.sysconfig.DictTypeQury;

import com.zeroone.star.sysconfig.entity.Dict;
import com.zeroone.star.sysconfig.entity.DictType;
import com.zeroone.star.sysconfig.mapper.DictMapper;
import com.zeroone.star.sysconfig.mapper.DictTypeMapper;
import com.zeroone.star.sysconfig.service.IDictTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * <p>
 * 字典类型 服务实现类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements IDictTypeService {
    @Resource
    DictMapper dictMapper;
    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Override
    public PageDTO<DictTypeDTO> getDictTypeList(DictTypeQury dictTypeQury) {
        Page<DictType> page = Page.of(dictTypeQury.getPageIndex(), dictTypeQury.getPageSize());
        Page<DictType> result = this.lambdaQuery()
                .like(dictTypeQury.getName() != null, DictType::getName, dictTypeQury.getName())
                .like(dictTypeQury.getId() != null, DictType::getId, dictTypeQury.getId())
                .like(dictTypeQury.getCode() != null, DictType::getCode, dictTypeQury.getCode())
                .like(dictTypeQury.getRemark() != null, DictType::getRemark, dictTypeQury.getRemark())
                .page(page);

        return PageDTO.create(result, dictType->{
            DictTypeDTO dto = new DictTypeDTO();
            BeanUtil.copyProperties(dictType, dto);
            return dto;

        });
    }


    @Override
    public List<DictTypeNameQuery> getDictTypeNameList() {
     List<DictType> list= dictTypeMapper.selectList(null);
     if(list==null){return new ArrayList<>();}
     List<DictTypeNameQuery> l=new ArrayList<>();
     for(DictType dd:list){
         DictTypeNameQuery d=new DictTypeNameQuery();
         d.setName(dd.getName());
         d.setId(dd.getId());
         l.add(d);
     }
        return l;
    }



    @Override
    public JsonVO<String> addDictType(DictTypeDTO dictTypeDTO) {
        if (dictTypeDTO.getName() == null || dictTypeDTO.getName().trim().isEmpty()) {
            return JsonVO.fail("字典类型名称不能为空");
        }

        if (dictTypeDTO.getCode() == null || dictTypeDTO.getCode().trim().isEmpty()) {
            return JsonVO.fail("字典类型编码不能为空");
        }
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", dictTypeDTO.getCode());
        DictType isexist = this.getOne(queryWrapper);
        if (isexist != null) {
            return JsonVO.fail("此编码已存在");
        }
        DictType dictType = new DictType();
        BeanUtils.copyProperties(dictTypeDTO, dictType);
        dictType.setId(java.util.UUID.randomUUID().toString().replace("-", ""));

        boolean result = this.save(dictType);
        if (result) {
            return JsonVO.success("新增成功");
        } else {
            return JsonVO.fail("新增失败");
        }
    }

    @Override
    public JsonVO<String> updateDictType(String id, DictTypeUpdateDTO updateDTO) {
        if (updateDTO.getName() == null || updateDTO.getName().trim().isEmpty()) {
            return JsonVO.fail("字典类型名称不能为空");
        }

        if (updateDTO.getCode() == null || updateDTO.getCode().trim().isEmpty()) {
            return JsonVO.fail("字典类型编码不能为空");
        }
        DictType exist = this.getById(updateDTO.getId());
        if (exist == null) {
            return JsonVO.fail("字典类型不存在");
        }

        Boolean nameexist = this.lambdaQuery()
                .eq(DictType::getName, updateDTO.getName())
                .ne(DictType::getId, updateDTO.getId())
                .exists();

        if (nameexist) {
            return JsonVO.fail("字典类型已经存在");
        }
        DictType dictType = new DictType();
        BeanUtils.copyProperties(updateDTO, dictType);
        boolean result = this.updateById(dictType);
        if (result) {
            return JsonVO.success("修改成功");
        } else {
            return JsonVO.fail("修改失败");
        }
    }

    @Override
    public JsonVO<String> deleteDictType(String id) {
        // 参数校验
        if (id == null || id.trim().isEmpty()) {
            return JsonVO.fail("ID参数不能为空");
        }

        // 检查字典类型是否存在
        DictType exist = this.getById(id);
        if (exist == null) {
            return JsonVO.fail("字典类型不存在");
        }

        // 检查是否存在关联字典项
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tid", id);
        Long dictCount = dictMapper.selectCount(queryWrapper);

        if (dictCount > 0) {
            return JsonVO.fail("该字典类型下存在 " + dictCount + " 个关联字典项，无法删除");
        }

        // 执行删除
        try {
            boolean result = this.removeById(id);
            return result ? JsonVO.success("删除成功") : JsonVO.fail("删除失败");
        } catch (Exception e) {
            return JsonVO.fail("删除过程中发生错误: " + e.getMessage());
        }
    }
}
