package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.storemanagement.entity.BatchDO;
import com.zeroone.star.storemanagement.service.impl.TransferServiceImpl;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.storemanagement.mapper
 * @Author: 高
 * @CreateTime: 2025-10-24 20:18
 * @Description: 批次查询数据访问层
 * @Version: 1.0
 */
@Mapper
public interface BatchMapper extends BaseMapper<BatchDO> {

    @Select("SELECT nums FROM batch WHERE number = #{batchNo} AND goods = #{goodsId} AND warehouse = #{warehouseId}")
    BigDecimal getBatchStock(@Param("batchNo") String batchNo, @Param("goodsId") String goodsId,
                             @Param("warehouseId") String warehouseId);

    @Update("UPDATE batch SET nums = nums + #{nums} WHERE number = #{batchNo} AND goods = #{goodsId} AND warehouse = #{warehouseId}")
    int updateBatchStock(@Param("batchNo") String batchNo, @Param("goodsId") String goodsId,
                         @Param("warehouseId") String warehouseId, @Param("nums") BigDecimal nums);

    @Update("INSERT INTO batch (id, room, warehouse, goods, number, time, nums) VALUES (#{id}, #{room}, #{warehouseId}, #{goodsId}, #{number}, #{time}, #{nums})")
    int createBatchInTargetWarehouse(@Param("id") String id, @Param("room") String room, @Param("warehouseId") String warehouseId, @Param("goodsId") String goodsId,
                                     @Param("number") String number, @Param("time") LocalDate  time, @Param("nums") BigDecimal nums);

    @Select("SELECT id FROM batch WHERE number = #{batchNo} AND goods = #{goodsId} AND warehouse = #{warehouseId}")
    String getBatchId(@Param("batchNo") String batchNo, @Param("goodsId") String goodsId,
                       @Param("warehouseId") String warehouseId);

    @Select("SELECT time FROM batch WHERE number = #{batchNo}")
    LocalDate getTimeByBatchNo(@Param("batchNo") String batchNo);

    /**
     * 批量检查批次是否存在
     */
    List<String> getExistingBatchNos(@Param("batchNos") List<String> batchNos);

    /**
     * 批量查询批次库存
     */
    @MapKey("batchNo")
    List<Map<String, Object>> getBatchStocks(@Param("paramList") List<TransferServiceImpl.StockQueryParam> paramList);

    @Select("SELECT * FROM batch WHERE room = #{id} AND number = #{batch}")
    BatchDO getByRoomAndNumber(String id, String batch);
}
