package com.zeroone.star.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.finance.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-10-31
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Select("SELECT  * FROM user WHERE name = #{name}")
    User  existsByName(String name);
}
