package com.zeroone.star.basic_information.service.impl;


import com.zeroone.star.basic_information.entity.User;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.userDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MsUserMapper {
    userDto toDto(User entity);
}
