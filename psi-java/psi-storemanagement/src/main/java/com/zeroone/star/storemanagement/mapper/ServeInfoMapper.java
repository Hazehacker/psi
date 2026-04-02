package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.storemanagement.entity.ServeInfoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServeInfoMapper extends BaseMapper<ServeInfoDO> {
    @Delete("delete from serve_info where info=#{info}")
    void deleteByInfo(String id);
}
