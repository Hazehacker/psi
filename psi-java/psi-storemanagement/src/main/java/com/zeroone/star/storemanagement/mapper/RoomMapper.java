package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.storemanagement.entity.RoomDO;
import com.zeroone.star.storemanagement.service.impl.TransferServiceImpl;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RoomMapper extends BaseMapper<RoomDO> {

    @Select("select id, warehouse, goods, attr, nums from room where goods = #{goods}")
    RoomDO getByGoods(String goods);

    @Delete("delete from room where id = #{id}")
    void deleteById(String id);

    @Select("SELECT * FROM room WHERE goods = #{goodsId} AND warehouse = #{warehouseId}")
    RoomDO getRoomByGoodsAndWarehouse(@Param("goodsId") String goodsId, @Param("warehouseId") String warehouseId);

    @Update("UPDATE room SET nums = nums + #{nums} WHERE goods = #{goodsId} AND warehouse = #{warehouseId}")
    int updateRoomStock(@Param("goodsId") String goodsId, @Param("warehouseId") String warehouseId,
                        @Param("nums") BigDecimal nums);

    @Update("INSERT INTO room (id, goods, warehouse, attr, nums) VALUES (#{id}, #{goodsId}, #{warehouseId}, #{attr}, #{nums})")
    int createRoomInTargetWarehouse(@Param("id") String id, @Param("goodsId") String goodsId, @Param("warehouseId") String warehouseId,
                                    @Param("attr") String attr, @Param("nums") BigDecimal nums);

    @Select("SELECT id FROM room WHERE goods = #{goodsId} AND warehouse = #{warehouseId}")
    String getRoomId(@Param("goodsId") String goodsId, @Param("warehouseId") String warehouseId);

    /**
     * 批量检查仓库库存
     */
    @MapKey("goodsId")
    List<Map<String, Object>> getRoomStocks(@Param("paramList")List<TransferServiceImpl.StockQueryParam> paramList);

    @Select("SELECT * FROM room WHERE warehouse = #{warehouse} AND goods = #{goods} AND attr = #{attr}")
    RoomDO getByGoodsAndWarehouseAndAttr(String warehouse, String goods, String attr);
}
