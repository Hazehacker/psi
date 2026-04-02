package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j1.homepage.WareHouseRoomDataDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 仓储信息 Mapper 接口
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {
    List<WareHouseRoomDataDto> selectWareHouseRoomDto();
}
