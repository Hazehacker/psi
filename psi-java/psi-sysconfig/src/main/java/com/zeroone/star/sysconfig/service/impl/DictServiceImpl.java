package com.zeroone.star.sysconfig.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictNamesDTO;
import com.zeroone.star.project.query.j1.sysconfig.DictQuery;
import com.zeroone.star.sysconfig.entity.Dict;
import com.zeroone.star.sysconfig.entity.DictType;
import com.zeroone.star.sysconfig.mapper.DictMapper;
import com.zeroone.star.sysconfig.mapper.DictTypeMapper;
import com.zeroone.star.sysconfig.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典 服务实现类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {
    @Resource
    MsDictMapper ms;

    @Resource
    DictTypeMapper dictTypeMapper;

    public PageDTO<DictDTO> selectByCondition(DictQuery condition){
        Page<DictDTO> page = new Page<>(condition.getPageIndex(), condition.getPageSize());
        baseMapper.selectPageByCondition(page, condition);
        PageDTO<DictDTO> pageDTO = PageDTO.create(page);
        System.out.println(pageDTO);
        return pageDTO;
    }

    @Override
    public List<DictNamesDTO> selectByTcode(String typeCode) {
        LambdaQueryWrapper<DictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DictType::getId)
                .eq(DictType::getCode, typeCode);
        String id = dictTypeMapper.selectOne(wrapper).getId();
        return selectByTid(id);
    }

    @Override
    public List<DictNamesDTO> selectByTid(String typeId) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tid", typeId);
        return  baseMapper.selectList(queryWrapper).stream()
                .map(ms::dictToDictNamesDto).collect(Collectors.toList());
    }
}
