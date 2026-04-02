package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.storemanagement.entity.AttrDO;
import org.apache.ibatis.annotations.Select;

public interface AttrMapper extends BaseMapper<AttrDO> {
    @Select("select * from is_attr where id = #{id}")
    AttrDO selectById(String id);
}
