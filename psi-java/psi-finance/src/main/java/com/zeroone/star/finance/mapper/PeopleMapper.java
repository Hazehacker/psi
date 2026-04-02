package com.zeroone.star.finance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.finance.entity.People;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 人员管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-10-31
 */
@Mapper
public interface PeopleMapper extends BaseMapper<People> {


    @Select("SELECT  * FROM people WHERE name = #{name}")
    People  existsByName(String name);
}
