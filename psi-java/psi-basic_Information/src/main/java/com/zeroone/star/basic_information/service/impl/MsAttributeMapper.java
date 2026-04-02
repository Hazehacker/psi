package com.zeroone.star.basic_information.service.impl;

import com.zeroone.star.basic_information.entity.Attribute;
import com.zeroone.star.basic_information.entity.AttributeInfo;
import com.zeroone.star.project.dto.j6.basic_information.product_management.AttributeDTO;
import org.mapstruct.Mapper;

/**
 * 描述：属性对应MapStruct映射接口
 * @author 杨潇
 * @version 1.0.0
 */
@Mapper(componentModel = "spring")
public interface MsAttributeMapper {
    
    /**
     * 实体转DTO
     * @param entity 属性实体
     * @return 属性DTO
     */
    AttributeDTO attributeToAttributeDto(Attribute entity);

    /**
     * 属性值实体转DTO
     * @param entity 属性值实体
     * @return 属性值DTO
     */
    AttributeDTO.AttributeValueDTO attributeInfoToAttributeValueDto(AttributeInfo entity);
}

