package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.SellInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j1.homepage.DailySalesStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 销售单详情 Mapper 接口
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Mapper
public interface SellInfoMapper extends BaseMapper<SellInfo> {
    DailySalesStats selectDailySales(@Param("today") String today);

    BigDecimal getDailyGrossAmount(@Param("today") String today);
}
