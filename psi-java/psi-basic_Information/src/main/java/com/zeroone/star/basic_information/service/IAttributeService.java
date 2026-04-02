package com.zeroone.star.basic_information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.basic_information.entity.Attribute;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.AttributeDTO;
import com.zeroone.star.project.query.j6.basic_information.product_management.AttributeQuery;

/**
 * 属性服务接口
 * 
 * @author 杨潇
 * @since 2025-10-19
 */
public interface IAttributeService extends IService<Attribute> {

    /**
     * 分页查询属性列表（包含属性值）
     * 
     * @param query 查询条件
     * @return 属性列表
     */
    PageDTO<AttributeDTO> listAttributes(AttributeQuery query);
}
