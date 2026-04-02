package com.zeroone.star.basic_information.controller;

import com.zeroone.star.basic_information.entity.Customer;
import com.zeroone.star.basic_information.service.ISCustomerService;
import com.zeroone.star.basic_information.service.impl.CustomerServiceImpl;
import com.zeroone.star.basic_information.service.impl.MsCustomerMapper;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.CustomerAddDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.CustomerDTO;
import com.zeroone.star.project.j6.basic_information.customer_management.CustomerAPis;
import com.zeroone.star.project.query.j6.basic_information.customer_management.CustomerQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户， 控制器
 * @author cmanPro
 */
@RestController
@RequestMapping("/basic_information/customer")
@Api(tags = "客户管理")
@Validated
public class CustomerController implements CustomerAPis {
    @Resource
    ISCustomerService customerService;

    @Resource
    MsCustomerMapper msCustomerMapper;
    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @GetMapping("/query-all")
    @ApiOperation(value = "查询所有数据")
    public JsonVO<PageDTO<CustomerDTO>> queryAll(@Validated CustomerQuery condition) {
        return JsonVO.success(customerService.listAll(condition));
    }

    @Override
    @GetMapping("/query-one")
    @ApiOperation("查询客户信息")
    @ApiImplicitParam(name = "id", value = "客户ID", required = true)
    public JsonVO<CustomerDTO> queryCustomerById(String id)
    {
        return JsonVO.success(customerService.getById(id));
    }

    @PostMapping("/add-customer")
    @ApiOperation("添加客户")
    @Override
    public JsonVO<String> addCustomer(@Validated @RequestBody CustomerAddDTO customerAddDTO) {
        Customer customer = msCustomerMapper.addDtoToCustomer(customerAddDTO);
        if(customerService.save(customer))
        {
            return JsonVO.success(customer.getId());
        }
        return JsonVO.fail(null);
    }

    @PutMapping("/update-customer")
    @ApiOperation("修改客户信息")
    @Override
    public JsonVO<String> updateCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        if(customerService.updateById(msCustomerMapper.customerDtoToCustomer(customerDTO))){
            return JsonVO.success(customerDTO.getId());
        }
        return JsonVO.fail(null);
    }

    @DeleteMapping("/delete-customer-list")
    @ApiOperation("删除客户")
    @Override
    public JsonVO<List<String>> deleteCustomer( @ApiParam(value = "客户ID列表", required = true, example = "[\"1\", \"2\"]") @RequestBody List<String> ids) {
        if(customerService.removeByIds(ids)){
            return JsonVO.success(ids);
        }
        return JsonVO.fail(null);
    }
}
