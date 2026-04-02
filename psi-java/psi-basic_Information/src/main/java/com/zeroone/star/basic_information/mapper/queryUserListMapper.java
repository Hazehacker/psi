package com.zeroone.star.basic_information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.basic_information.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface queryUserListMapper extends BaseMapper<User> {
}
