package com.zeroone.star.finance.mapper.otherexpense;

import com.zeroone.star.finance.entity.OceDetailViewDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OceDetailViewMapper {
    OceDetailViewDO selectById(@Param("id") String id);
}
