package com.zeroone.star.supportinfo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.*;
import com.zeroone.star.project.query.PageQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.supportattri.AttributeQuery;
import com.zeroone.star.supportinfo.entity.Attribute;
import com.zeroone.star.supportinfo.entity.AttributeInfo;
import com.zeroone.star.supportinfo.mapper.AttributeMapper;
import com.zeroone.star.supportinfo.service.IAttributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 辅助属性[基础] 服务实现类
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Service
public class AttributeServiceImpl extends ServiceImpl<AttributeMapper, Attribute> implements IAttributeService {

    @Resource
    MsAttributeMapper ms;

    @Override
    public PageDTO<AttributeDTO> listSupportAttri(AttributeQuery query) {
        // 构建分页查询对象
        Page<Attribute> page = new Page<>(query.getPageIndex(), query.getPageSize());
        // 构建查询条件对象
        QueryWrapper<Attribute> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(query.getName()), "name", query.getName());
        queryWrapper.like(!StringUtils.isEmpty(query.getData()), "data", query.getData());
        queryWrapper.orderBy(true, true, "sort");
        Page<Attribute> pageRes = baseMapper.selectPage(page, queryWrapper);
        return PageDTO.create(pageRes, ms::attributeToAttributeDTO);
    }

    @Override
    public AttributeDetailDTO getSupportAttriDetail(String id) {
        // 获取查询的辅助属性对象
        Attribute attribute = baseMapper.selectById(id);
        if (attribute == null) {
            return null;
        }
        // 获取属性内容对象同时转为DTO
        List<AttributeInfoDTO> contentDTOs = baseMapper.selectContentByPid(attribute.getId()).stream()
                .map(content -> new AttributeInfoDTO(content.getName()))
                .collect(Collectors.toList());
        AttributeDetailDTO attributeDetailDTO = new AttributeDetailDTO();
        attributeDetailDTO.setId(attribute.getId());
        attributeDetailDTO.setName(attribute.getName());
        attributeDetailDTO.setSort(attribute.getSort());
        attributeDetailDTO.setData(attribute.getData());
        attributeDetailDTO.setContent(contentDTOs);
        return attributeDetailDTO;
    }

    @Override
    public PageDTO<AttributeDetailDTO> listSupportAttriList(PageQuery query) {
        // 创建分页对象
        Page<Attribute> page = new Page<>(query.getPageIndex(), query.getPageSize());
        // 构建查询条件对象
        QueryWrapper<Attribute> queryWrapper = new QueryWrapper<>();
        // 执行分页查询
        Page<Attribute> pageAttrs = baseMapper.selectPage(page, queryWrapper);

        // 转换为DTO
        List<AttributeDetailDTO> details = pageAttrs.getRecords().stream()
                .map(attr -> {
                    AttributeDetailDTO dto = new AttributeDetailDTO();
                    dto.setId(attr.getId());
                    dto.setName(attr.getName());
                    dto.setSort(attr.getSort());
                    dto.setData(attr.getData());
                    // 查询当前属性的所有子属性内容
                    List<AttributeInfo> contents = baseMapper.selectContentByPid(attr.getId());
                    List<AttributeInfoDTO> contentDTOs = contents.stream()
                            .map(content -> new AttributeInfoDTO(content.getName()))
                            .collect(Collectors.toList());
                    dto.setContent(contentDTOs);
                    return dto;
                })
                .collect(Collectors.toList());

        // 创建Page对象
        Page<AttributeDetailDTO> pageRes = new Page<>(query.getPageIndex(), query.getPageSize());
        pageRes.setTotal(pageAttrs.getTotal());
        pageRes.setRecords(details);

        return PageDTO.create(pageRes);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAttribute(AddAttributeDTO addAttributeDTO) {
        // 检查属性名称是否重复
        if (isNameDuplicate(addAttributeDTO.getName())) {
            throw new RuntimeException("属性名称已存在");
        }

        // 插入辅助属性[基础]表
        Attribute attribute = new Attribute();
        attribute.setName(addAttributeDTO.getName());
        attribute.setSort(addAttributeDTO.getSort());
        attribute.setData(addAttributeDTO.getData());
        int res = baseMapper.insert(attribute);

        // 插入属性详情表
        List<AttributeInfo> infos = addAttributeDTO.getContent().stream()
                .map(content -> {
                    AttributeInfo info = new AttributeInfo();
                    info.setId(IdUtil.simpleUUID());
                    info.setName(content.getName());
                    info.setPid(attribute.getId());
                    return info;
                })
                .collect(Collectors.toList());
        baseMapper.insertBatchAttributeInfo(infos);

        if(res>0)return attribute.getId();
        else return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSupportAttri(String id) {
        baseMapper.deleteContentByPid(id);
        int res = baseMapper.deleteById(id);
        return res > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSupportAttri(UpdateAttributeDTO updateAttributeDTO) {
        // 获取现有数据
        Attribute existingAttribute = baseMapper.selectById(updateAttributeDTO.getId());
        if (existingAttribute == null) {
            throw new RuntimeException("属性不存在");
        }

        // 只有当名称真正改变时才检查重复
        if (!existingAttribute.getName().equals(updateAttributeDTO.getName())) {
            if (isNameDuplicate(updateAttributeDTO.getName(), updateAttributeDTO.getId())) {
                throw new RuntimeException("属性名称已存在");
            }
        }

        // 1. 主表更新
        Attribute attribute = new Attribute();
        attribute.setId(updateAttributeDTO.getId());
        attribute.setData(updateAttributeDTO.getData());
        attribute.setSort(updateAttributeDTO.getSort());
        attribute.setName(updateAttributeDTO.getName());
        int res = baseMapper.updateById(attribute);

        // 2. 子表先删后插
        baseMapper.deleteContentByPid(attribute.getId());
        if (CollUtil.isNotEmpty(updateAttributeDTO.getContent())) {
            List<AttributeInfo> infos = updateAttributeDTO.getContent()
                    .stream()
                    .map(content -> {
                        AttributeInfo info = new AttributeInfo();
                        info.setId(IdUtil.simpleUUID());
                        info.setPid(attribute.getId());
                        info.setName(content.getName());
                        return info;
                    })
                    .collect(Collectors.toList());
            baseMapper.insertBatchAttributeInfo(infos);
        }
        return res > 0;
    }

    /**
     * 检查属性名称是否重复（排除指定ID）
     */
    private boolean isNameDuplicate(String name, String excludeId) {
        QueryWrapper<Attribute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        if (StringUtils.isNotBlank(excludeId)) {
            queryWrapper.ne("id", excludeId);
        }
        return baseMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 检查属性名称是否重复
     */
    private boolean isNameDuplicate(String name) {
        return isNameDuplicate(name, null);
    }
}
