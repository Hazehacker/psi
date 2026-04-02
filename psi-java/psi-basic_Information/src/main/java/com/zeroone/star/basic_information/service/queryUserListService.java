package com.zeroone.star.basic_information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.basic_information.entity.User;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.userDto;
import com.zeroone.star.project.query.j6.basic_information.customer_management.userListQuery;

public interface queryUserListService extends IService<User> {
    PageDTO<userDto> queryUserList(userListQuery query);
}
