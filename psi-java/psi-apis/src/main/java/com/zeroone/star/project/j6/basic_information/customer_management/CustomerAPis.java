package com.zeroone.star.project.j6.basic_information.customer_management;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.CustomerAddDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.CustomerDTO;
import com.zeroone.star.project.query.j6.basic_information.customer_management.CustomerQuery;
import com.zeroone.star.project.query.j6.basic_information.supplier_management.supplierListQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j6.basic_information.customer_management.CustomerVO;

import com.zeroone.star.project.vo.j6.basic_information.supplier_management.ImportResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * cmanPro
 * 客户API
 */
public interface CustomerAPis {

    /**
     * 分页查询所有客户列表
     * @param condition
     * @return
     */
    JsonVO<PageDTO<CustomerDTO>> queryAll(CustomerQuery condition);

    /**
     * 获取指定客户信息
     * @param id 客户ID
     * @return 客户信息
     */
    JsonVO<CustomerDTO> queryCustomerById(String id);

    /**
     * 添加客户
     * @param customerAddDTO 客户信息
     * @return 添加结果
     */
    JsonVO<String> addCustomer(CustomerAddDTO customerAddDTO);

    /**
     * 修改客户信息
     * @param customerDTO 客户信息
     * @return 修改结果
     */
    JsonVO<String> updateCustomer(CustomerDTO customerDTO);

    /**
     * 删除客户（支持批量）
     * @param ids 供应商ID列表
     * @return 删除结果
     */
    JsonVO<List<String>> deleteCustomer(List<String> ids);


}
