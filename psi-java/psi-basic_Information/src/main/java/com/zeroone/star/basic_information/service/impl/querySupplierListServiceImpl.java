package com.zeroone.star.basic_information.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.basic_information.entity.Supplier;
import com.zeroone.star.basic_information.mapper.querySupplierListMapper;
import com.zeroone.star.basic_information.service.querySupplierListService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.querySupplierListDto;
import com.zeroone.star.project.query.j6.basic_information.supplier_management.supplierListQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class querySupplierListServiceImpl extends ServiceImpl<querySupplierListMapper, Supplier> implements querySupplierListService {
    @Resource
    MsSupplierMapper ms;

    public PageDTO<querySupplierListDto> querySupplierList(supplierListQuery  query) {
        // 构建分页查询对象
        Page<Supplier> page = new Page<>(query.getPageIndex(), query.getPageSize());

        QueryWrapper<Supplier> wrapper = new QueryWrapper<>();
        // 相似条件查询（使用 like）
        if (StringUtils.isNotBlank(query.getName())) {
            wrapper.like("name", query.getName());
        }
        if (StringUtils.isNotBlank(query.getNumber())) {
            wrapper.like("number", query.getNumber());
        }
        if (StringUtils.isNotBlank(query.getCategory())) {
            wrapper.like("category", query.getCategory());
        }
        if (StringUtils.isNotBlank(query.getContacts())) {
            wrapper.like("contacts", query.getContacts());
        }
        if (StringUtils.isNotBlank(query.getPhone())) {
            wrapper.like("phone", query.getPhone());
        }
        if (StringUtils.isNotBlank(query.getData())) {
            wrapper.like("data", query.getData());
        }
        if (query.getUser() != null && StringUtils.isNotBlank(query.getUser().toString())) {
            wrapper.like("user", query.getUser());
        }


        Page<Supplier> pageRes= baseMapper.selectPage(page, wrapper);
        return PageDTO.create(pageRes,ms::toDto);
    }
}
