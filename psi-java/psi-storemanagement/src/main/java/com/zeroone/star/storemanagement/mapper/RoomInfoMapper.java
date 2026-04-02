package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.storemanagement.entity.RoomInfoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomInfoMapper extends BaseMapper<RoomInfoDO> {

    @Delete("delete from room_info where info = #{info}")
    void deleteByInfo(String id);

    @Delete("delete from room_info where info = #{swapInfoId}")
    int deleteBySwapInfoId(String swapInfoId);
}
