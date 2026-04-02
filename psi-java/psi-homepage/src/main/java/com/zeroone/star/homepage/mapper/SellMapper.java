package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.Sell;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j1.homepage.SalesDailyTotalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 销售单 Mapper 接口
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Mapper
public interface SellMapper extends BaseMapper<Sell> {

    /**
     * 查询销售单每日汇总数据
     * @param startDate
     * @param endDate
     * @return
     */
    @Select("SELECT DATE(time) as date, SUM(total) as value " +
            "FROM sell " +
            "WHERE examine = 1 " +
            "  AND time >= #{startDate} " +
            "  AND time <= #{endDate} " +
            "GROUP BY DATE(time)" +
            "ORDER BY date")
    List<SalesDailyTotalDTO> selectSalesDailyTotal(@Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);
}
