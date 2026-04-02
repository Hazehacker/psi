package com.zeroone.star.basic_information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.basic_information.entity.Supplier;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.querySupplierListDto;
import com.zeroone.star.project.query.j6.basic_information.supplier_management.supplierListQuery;


public interface querySupplierListService extends IService<Supplier> {

    /**
     * 查询供应商列表
     * @param query 查询参数
     * @return 供应商列表
     */
    PageDTO<querySupplierListDto> querySupplierList(supplierListQuery query);
}
