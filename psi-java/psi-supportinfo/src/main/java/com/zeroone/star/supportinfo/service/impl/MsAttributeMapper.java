package com.zeroone.star.supportinfo.service.impl;

import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.AttributeDTO;
import com.zeroone.star.supportinfo.entity.Attribute;
import org.mapstruct.Mapper;

/**
 * <p>
 * 描述：Attribute对应MapStruct映射接口
 * </p>
 *
 * @author yi
 * @version 1.0.0
 */
@Mapper(componentModel = "spring")
public interface MsAttributeMapper {
    /**
     * 映射DTO
     * @param entity 实体
     * @return DTO
     */
    AttributeDTO attributeToAttributeDTO(Attribute entity);

}
