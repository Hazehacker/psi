package com.zeroone.star.project.j6.basic_information.customer_management;

import com.zeroone.star.project.dto.j6.basic_information.supplier_management.SupplierAddDTO;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.SupplierDTO;
import com.zeroone.star.project.vo.JsonVO;

import java.util.List;

/**
 * @author 趣味生煎
 * @version 1.0
 * 描述：
 */
public interface SupplierApis {

    /**
     * 获取指定供应商信息
     * @param id 供应商ID
     * @return 供应商信息
     */
    JsonVO<SupplierDTO> querySupplierById(Integer id);

    /**
     * 添加供应商
     * @param supplierAddDTO 供应商信息
     * @return 添加结果
     */
    JsonVO<String> addSupplier(SupplierAddDTO supplierAddDTO);

    /**
     * 修改供应商信息
     * @param supplierDTO 供应商信息
     * @return 修改结果
     */
    JsonVO<String> updateSupplier(SupplierDTO supplierDTO);

    /**
     * 删除供应商（支持批量）
     * @param ids 供应商ID列表
     * @return 删除结果
     */
    JsonVO<String> deleteSupplier(List<Integer> ids);


}
