package com.zeroone.star.supportinfo.mapper;

import com.zeroone.star.supportinfo.entity.Attribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.supportinfo.entity.AttributeInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 辅助属性[基础] Mapper 接口
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Mapper
public interface AttributeMapper extends BaseMapper<Attribute> {

    /**
     * 根据属性ID查询属性内容表中的记录
     * @param pid 关联的属性id
     * @return 属性内容列表
     */
    @Select("SELECT id, name FROM attribute_info WHERE pid = #{pid}")
    List<AttributeInfo> selectContentByPid(@Param("pid") String pid);

    /**
     * 根据属性ID删除属性内容表中的记录
     *
     * @param pid 属性ID
     */
    @Delete("DELETE FROM attribute_info WHERE pid = #{pid}")
    void deleteContentByPid(@Param("pid") String pid);

    /**
     * 自定义批量插入属性内容的方法
     * @param list 要批量插入的属性内容数据
     */
    void insertBatchAttributeInfo (@Param("list") List<AttributeInfo> list);
}
