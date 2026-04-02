package com.zeroone.star.purchase.mapper;

import com.zeroone.star.purchase.DO.RoomDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 仓储信息 Mapper 接口
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
@Mapper
public interface RoomMapper extends BaseMapper<RoomDO> {
    /**
     * 更新库存数量（基于仓库+商品联合条件）
     */
    int updateNumsByWarehouseAndGoods(@Param("warehouse") String warehouse,
                                      @Param("goods") String goods,
                                      @Param("nums") BigDecimal nums);
}
