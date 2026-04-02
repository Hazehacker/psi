package com.zeroone.star.homepage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.homepage.entity.Room;
import com.zeroone.star.homepage.mapper.RoomMapper;
import com.zeroone.star.homepage.service.IRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j1.homepage.WareHouseRoomDataDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 仓储信息 服务实现类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {
    @Resource
    RoomMapper roomMapper;
    @Override
    public List<WareHouseRoomDataDto> listWareHouseRoomData() {
        return roomMapper.selectWareHouseRoomDto();
    }
}
