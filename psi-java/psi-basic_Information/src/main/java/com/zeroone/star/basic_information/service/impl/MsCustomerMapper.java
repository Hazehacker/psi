package com.zeroone.star.basic_information.service.impl;

import com.zeroone.star.basic_information.entity.Customer;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.CustomerAddDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.CustomerDTO;
import org.mapstruct.Mapper;


/**
 * 客户对应的MapStuct映射接口
 * @author cmanPro
 */
@Mapper(componentModel = "spring")
public interface MsCustomerMapper {
    /**
     * 映射DTO
     * @param entity 实体
     * @return DTO
     */
    CustomerDTO customerToCustomerDTO(Customer entity);

    /**
     * 映射实体
     * @param dto CustomerAddDTO
     * @return 实体
     */
    Customer addDtoToCustomer(CustomerAddDTO dto);

    /**
     * 映射实体
     * @param dto CustomerDTO
     * @return 实体
     */
    Customer customerDtoToCustomer(CustomerDTO dto);
}
