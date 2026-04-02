package com.zeroone.star.sysconfig.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;

import com.zeroone.star.project.dto.j1.sysconfig.DictDTO;
import com.zeroone.star.project.query.j1.sysconfig.DictQuery;
import com.zeroone.star.sysconfig.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

import java.util.List;

/**
 * <p>
 * 字典 Mapper 接口
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {
    /**
     * 获取字典列表(条件)
     * */
    List<DictDTO> selectByCondition(@Param("condition") DictQuery condition);

    /**
     * 获取字典列表(分页 + 条件)
     * */
    IPage<DictDTO> selectPageByCondition(IPage<DictDTO> page, @Param("condition") DictQuery condition);

}
