package com.zeroone.star.supportinfo.mapper;

import com.zeroone.star.supportinfo.entity.Often;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 常用功能 Mapper 接口
 * </p>
 *
 * @author 不易
 * @since 2025-10-26
 */
@Mapper
public interface OftenMapper extends BaseMapper<Often> {
    @Select("SELECT `key` FROM often WHERE user = #{userId}")
    List<String> selectConfiguredKeys(@Param("userId") String userId);

    @Select("SELECT * FROM often WHERE user = #{userId} ORDER BY id")
    List<Often> selectByUser(@Param("userId") String userId);

    @Delete("DELETE FROM often WHERE user = #{userId}")
    void deleteByUser(@Param("userId") String userId);
}
