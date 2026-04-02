package com.zeroone.star.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zeroone.star.sale.entity.RoomInfo;
import com.zeroone.star.sale.entity.SreInfo;
import com.zeroone.star.sale.mapper.RoomInfoplusMapper;
import com.zeroone.star.sale.service.IRoomInfoplusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 仓储详情 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-10-26
 */
@Service
public class RoomInfoplusServiceImpl extends ServiceImpl<RoomInfoplusMapper, RoomInfo> implements IRoomInfoplusService {

    @Override
    public void saveBySreInfo(List<SreInfo> list) {
        List<RoomInfo> roomInfos=new ArrayList<>();
        for (SreInfo sreInfo : list) {
            RoomInfo roomInfo=new RoomInfo();
            roomInfo.setPid(sreInfo.getWarehouse());
            roomInfo.setType("sre");
            roomInfo.setClassName(sreInfo.getPid());
            roomInfo.setInfo(sreInfo.getId());
            roomInfo.setTime(LocalDateTime.now());
            roomInfo.setDirection(1);
            roomInfo.setPrice(sreInfo.getPrice());
            roomInfo.setNums(sreInfo.getNums());
            roomInfos.add(roomInfo);
        }
        saveBatch(roomInfos);
    }

    @Override
    public void deleteBySreInfo(List<SreInfo> list) {
        for (SreInfo sreInfo : list) {
            String pid = sreInfo.getPid();
            remove(new LambdaQueryWrapper<RoomInfo>().eq(RoomInfo::getClassName, pid));
        }
    }


}
