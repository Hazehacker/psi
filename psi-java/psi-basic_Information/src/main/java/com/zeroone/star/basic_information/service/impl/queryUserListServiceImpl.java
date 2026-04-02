package com.zeroone.star.basic_information.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.basic_information.entity.User;
import com.zeroone.star.basic_information.mapper.queryUserListMapper;
import com.zeroone.star.basic_information.service.queryUserListService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.userDto;
import com.zeroone.star.project.query.j6.basic_information.customer_management.userListQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class queryUserListServiceImpl extends ServiceImpl<queryUserListMapper, User> implements queryUserListService {
    @Resource
    MsUserMapper ms;
    @Override
    public PageDTO<userDto> queryUserList(userListQuery query) {
        Page<User> page = new Page<>(query.getPageIndex(), query.getPageSize());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 动态添加查询条件，避免空值查询影响性能
        if (StringUtils.hasText(query.getName())) {
            wrapper.like("name", query.getName());
        }
        if (StringUtils.hasText(query.getUser())) {
            wrapper.like("user", query.getUser());
        }
        if (StringUtils.hasText(query.getTel())) {
            wrapper.like("tel", query.getTel());
        }
        if (StringUtils.hasText(query.getData())) {
            wrapper.like("data", query.getData());
        }
        Page<User> pageRes= baseMapper.selectPage(page, wrapper);
        // 使用自定义转换逻辑替代 ms::toDto
        return PageDTO.create(pageRes, ms::toDto);

    }
}
