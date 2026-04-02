package com.zeroone.star.sysconfig.service.impl;

import com.zeroone.star.project.dto.j1.sysconfig.DictNamesDTO;
import com.zeroone.star.sysconfig.entity.Dict;
import org.mapstruct.Mapper;

/**
 * <p>
 * 描述：Dict对应MapStruct映射接口
 * </p>
 */
@Mapper(componentModel = "spring")
public interface MsDictMapper {

    DictNamesDTO dictToDictNamesDto(Dict entity);

}
