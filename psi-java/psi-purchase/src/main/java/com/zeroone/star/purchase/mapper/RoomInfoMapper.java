package com.zeroone.star.purchase.mapper;

import com.zeroone.star.purchase.DO.RoomInfoDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 仓储详情 Mapper 接口
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Mapper
@Component("purchaseRoomInfoMapper")
public interface RoomInfoMapper extends BaseMapper<RoomInfoDO> {

}
