package com.zeroone.star.basic_information.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.basic_information.entity.Attribute;
import com.zeroone.star.basic_information.entity.AttributeInfo;
import com.zeroone.star.basic_information.mapper.AttributeInfoMapper;
import com.zeroone.star.basic_information.mapper.AttributeMapper;
import com.zeroone.star.basic_information.service.IAttributeService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.AttributeDTO;
import com.zeroone.star.project.query.j6.basic_information.product_management.AttributeQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 属性服务实现类
 * 
 * @author 杨潇
 * @since 2025-10-19
 */
@Service
public class AttributeServiceImpl extends ServiceImpl<AttributeMapper, Attribute> implements IAttributeService {

    @Resource
    MsAttributeMapper msAttributeMapper;

    @Resource
    AttributeInfoMapper attributeInfoMapper;

    @Override
    public PageDTO<AttributeDTO> listAttributes(AttributeQuery query) {
        // 构建分页查询对象
        Page<Attribute> page = new Page<>(query.getPageIndex(), query.getPageSize());

        // 构建查询条件
        QueryWrapper<Attribute> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(query.getName()), "name", query.getName());
        queryWrapper.orderByAsc("sort", "id");

        // 分页查询
        Page<Attribute> pageRes = baseMapper.selectPage(page, queryWrapper);

        // 获取所有属性ID
        List<String> attributeIds = pageRes.getRecords().stream()
                .map(Attribute::getId)
                .collect(Collectors.toList());

        // 批量查询属性值
        Map<String, List<AttributeDTO.AttributeValueDTO>> attributeValuesMap = null;
        if (!attributeIds.isEmpty()) {
            QueryWrapper<AttributeInfo> valueQuery = new QueryWrapper<>();
            valueQuery.in("pid", attributeIds);
            List<AttributeInfo> attributeInfoList = attributeInfoMapper.selectList(valueQuery);

            // 按属性ID分组
            attributeValuesMap = attributeInfoList.stream()
                    .collect(Collectors.groupingBy(
                            AttributeInfo::getPid,
                            Collectors.mapping(
                                    msAttributeMapper::attributeInfoToAttributeValueDto,
                                    Collectors.toList())));
        }

        // 转换并填充属性值列表
        final Map<String, List<AttributeDTO.AttributeValueDTO>> finalValuesMap = attributeValuesMap;
        return PageDTO.create(pageRes, attribute -> {
            AttributeDTO dto = msAttributeMapper.attributeToAttributeDto(attribute);
            if (finalValuesMap != null) {
                dto.setValues(finalValuesMap.get(attribute.getId()));
            }
            return dto;
        });
    }
}
