package com.zeroone.star.project.j6.basic_information.customer_management;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.userDto;
import com.zeroone.star.project.query.j6.basic_information.customer_management.userListQuery;
import com.zeroone.star.project.vo.JsonVO;

public interface userApis {
    /**
     * 查询用户列表
     * @param query 查询参数
     * @return 用户列表
     */
    JsonVO<PageDTO<userDto>> queryUserList(userListQuery query);
}
