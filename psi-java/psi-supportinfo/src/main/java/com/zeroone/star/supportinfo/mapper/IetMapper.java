package com.zeroone.star.supportinfo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.inandexpend.IetNameDTO;
import com.zeroone.star.supportinfo.entity.Iet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 收支类别 Mapper 接口
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Mapper
public interface IetMapper extends BaseMapper<Iet> {

    /**
     * 按收支类别查询收支名称
     * @param queryWrapper
     * @return
     */
    @Select("select name,id from iet ${ew.customSqlSegment}")
    List<IetNameDTO> selectNameByType(@Param("ew") QueryWrapper<Iet> queryWrapper);
}
