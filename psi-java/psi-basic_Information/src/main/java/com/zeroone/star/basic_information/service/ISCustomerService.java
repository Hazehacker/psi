package com.zeroone.star.basic_information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.basic_information.entity.Customer;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.CustomerDTO;
import com.zeroone.star.project.query.j6.basic_information.customer_management.CustomerQuery;

/**
 * 客户，服务
 * @author cmanPro
 */

public interface ISCustomerService extends IService<Customer> {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    CustomerDTO getById(String id);

    /**
     * 查询所有
     * @param query
     * @return
     */
    PageDTO<CustomerDTO> listAll(CustomerQuery query);
}


