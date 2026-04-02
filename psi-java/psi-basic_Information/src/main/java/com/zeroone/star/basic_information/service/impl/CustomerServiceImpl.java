package com.zeroone.star.basic_information.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.basic_information.entity.Customer;
import com.zeroone.star.basic_information.mapper.CustomerMapper;
import com.zeroone.star.basic_information.mapper.CustomerMapper;
import com.zeroone.star.basic_information.service.ISCustomerService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.CustomerDTO;
import com.zeroone.star.project.query.j6.basic_information.customer_management.CustomerQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Service实现
 * @ author cmanPro
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ISCustomerService {
    @Resource
    MsCustomerMapper msCustomerMapper;

    @Override
    public CustomerDTO getById(String id) {
        Customer customer = baseMapper.selectById(id);
        if(customer == null)
        {
            return null;
        }
        return msCustomerMapper.customerToCustomerDTO(customer);
    }

    public PageDTO<CustomerDTO> listAll(CustomerQuery query) {
        // 构建分页查询对象
        Page<Customer> page = new Page<>(query.getPageIndex(), query.getPageSize());
        // 构建查询条件对象
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(query.getName()), "name", query.getName());
        queryWrapper.like(!StringUtils.isEmpty(query.getNumber()), "number", query.getNumber());
        queryWrapper.like(!StringUtils.isEmpty(query.getCategory()), "category", query.getCategory());
        queryWrapper.like(!StringUtils.isEmpty(query.getGrade()), "grade", query.getGrade());
        queryWrapper.like(!StringUtils.isEmpty(query.getContactPerson()), "contacts", query.getContactPerson());
        queryWrapper.like(!StringUtils.isEmpty(query.getContactPhone()), "contacts", query.getContactPhone());
        queryWrapper.like(!StringUtils.isEmpty(query.getUser()), "user", query.getUser());
        queryWrapper.like(!StringUtils.isEmpty(query.getData()), "data", query.getData());
        //queryWrapper.orderBy(true, false, "IFNULL(`update_time`,`create_time`)");
        queryWrapper.orderBy(true, false, "id");
        // 分页查询
        Page<Customer> pageRes = baseMapper.selectPage(page, queryWrapper);
        System.out.println(pageRes);
        return PageDTO.create(pageRes, msCustomerMapper::customerToCustomerDTO);
    }

}
