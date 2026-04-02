package com.zeroone.star.sale.service.impl;

import com.zeroone.star.sale.entity.Room;
import com.zeroone.star.sale.mapper.RoomplusMapper;
import com.zeroone.star.sale.service.IRoomplusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 仓储信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-10-26
 */
@Service
public class RoomplusServiceImpl extends ServiceImpl<RoomplusMapper, Room> implements IRoomplusService {

    @Override
    public void updateAndsave(List<Room> rooms) {
        for (Room room : rooms) {
            Room one = query().eq("warehouse", room.getWarehouse())
                    .eq("goods", room.getGoods())
                    .eq("attr", room.getAttr()).one();
            if (one != null) {
                update().eq("id", one.getId())
                        .set("nums", one.getNums().add(BigDecimal.valueOf(1)))
                        .update();
                continue;
            }
            save(room);
        }
    }

    @Override
    public boolean updateAndDelete(List<Room> rooms) {
        for (Room room : rooms) {
            Room one = query().eq("warehouse", room.getWarehouse())
                    .eq("goods", room.getGoods())
                    .eq("attr", room.getAttr()).one();
            if(one==null||one.getNums().compareTo(BigDecimal.valueOf(0))==0){
                return false;
            }
            update().eq("id", one.getId())
                    .set("nums", one.getNums().add(BigDecimal.valueOf(-1)))
                    .update();
        }
        return true;
    }
}
