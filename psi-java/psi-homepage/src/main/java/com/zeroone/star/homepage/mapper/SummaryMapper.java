package com.zeroone.star.homepage.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;


@Mapper
public interface SummaryMapper {

    //获取的是用户所属frame的pid
    @Select({
            "SELECT T2.pid",
            "FROM user T1",
            "INNER JOIN frame T2 ON T1.frame = T2.id",
            "WHERE T1.id = #{curUserID}"
    })
    String getCur_FrameID(@Param("curUserID") String curUserID);


    /**
     * 1. 商品总数
     */
    @Select("SELECT COUNT(*) FROM goods")
    Long getTotalGoodsCount();

    /**
     * 2. 库存总数
     * (使用IFNULL确保表为空时返回0)
     */
    @Select("SELECT IFNULL(SUM(nums), 0) FROM room")
    BigDecimal getTotalStockSum();

    /**
     * 3. 客户总数 (根据frameId过滤)
     */
    @Select("SELECT COUNT(*) FROM customer WHERE frame = #{frameId}")
    Long getTotalCustomersCount(@Param("frameId") String frameId);

    /**
     * 4. 供应商总数 (根据frameId过滤)
     */
    @Select("SELECT COUNT(*) FROM supplier WHERE frame = #{frameId}")
    Long getTotalSuppliersCount(@Param("frameId") String frameId);

    /**
     * 5. 库存预警
     * (查询库存数量 room.nums 小于等于 商品表.stock 阈值的 *商品种类* 数量)
     */
    @Select({
            "SELECT COUNT(DISTINCT r.goods)",
            "FROM room r",
            "JOIN goods g ON r.goods = g.id",
            "WHERE g.stock > 0", // 仅当设置了库存阈值
            "AND r.nums <= g.stock"
    })
    Long getStockWarningCount();

    /**
     * 6. 保质期预警
     * (查询在库、开启保质期、且已进入预警期但尚未过期的商品 *种类* 数量)
     */
    @Select({
            "SELECT COUNT(DISTINCT r.goods)",
            "FROM room_info ri",
            "JOIN room r ON ri.pid = r.id",
            "JOIN goods g ON r.goods = g.id",
            "WHERE ri.direction = 1",       // 入库单据
            "AND g.validity = 1",         // 商品开启了保质期
            "AND r.nums > 0",             // 仓库中该商品还有库存
            "AND g.protect > 0",          // 设置了保质期天数
            "AND g.threshold > 0",        // 设置了预警阀值天数
            "AND g.protect > g.threshold",// 确保 (保质期 - 预警期) > 0
            "AND NOW() BETWEEN ",
            "    DATE_ADD(ri.time, INTERVAL (g.protect - g.threshold) DAY) ", // 预警开始时间
            "AND ",
            "    DATE_ADD(ri.time, INTERVAL g.protect DAY)"  // 实际过期时间
    })
    Long getExpiryWarningCount();
}