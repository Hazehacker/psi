package com.zeroone.star.sysconfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.sysconfig.entity.MenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface MenuMapper  extends BaseMapper<MenuDO> {
    /**
     * 递归查询所有以 parentId 为父节点的子节点 ID（包括所有层级的间接子节点）
     * @param parentId 父节点 ID（对应 XML 中的 #{parentId}）
     * @return 所有子节点 ID 的集合
     */
    List<String> selectAllChildIds(@Param("parentId") String parentId);

    void setRecursionDepth();


}
