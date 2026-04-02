package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.Room;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j1.homepage.WareHouseRoomDataDto;

import java.util.List;

/**
 * <p>
 * 仓储信息 服务类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
public interface IRoomService extends IService<Room> {
    List<WareHouseRoomDataDto> listWareHouseRoomData();
}
