package com.zeroone.star.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.finance.entity.Frame;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 组织机构 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-10-31
 */
@Mapper
public interface FrameMapper extends BaseMapper<Frame> {

    @Select("SELECT * FROM frame WHERE name = #{name}")
    Frame existsByName(String name);
}
