package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.Bre;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j1.homepage.PurchaseReturnDailyTotalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 采购退货单 Mapper 接口
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Mapper
public interface BreMapper extends BaseMapper<Bre> {


    /**
     * 查询销售单每日汇总数据
     * @param startDate
     * @param endDate
     * @return
     */
    @Select("SELECT DATE(time) as date, SUM(total) as value " +
            "FROM bre " +
            "WHERE examine = 1 " +
            "  AND time >= #{startDate} " +
            "  AND time <= #{endDate} " +
            "GROUP BY DATE(time)" +
            "ORDER BY date")
    List<PurchaseReturnDailyTotalDTO> selectPurchaseReturnDailyTotal(@Param("startDate") String startDate,
                                                                     @Param("endDate") String endDate);
}
