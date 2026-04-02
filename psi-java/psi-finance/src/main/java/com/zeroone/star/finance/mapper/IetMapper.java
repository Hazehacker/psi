package com.zeroone.star.finance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.finance.entity.Iet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 收支类别 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-11-01
 */

@Mapper
public interface IetMapper extends BaseMapper<Iet> {


    /**
     *
     * 根据name在iet表当中查找数据
     * @param name
     * @return
     */
    @Select("select * from iet where name = #{name}")
    Iet selectOneIet(String name);

}

