package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.AccountInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j1.homepage.DailyIncome;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 资金详情 Mapper 接口
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Mapper
public interface AccountInfoMapper extends BaseMapper<AccountInfo> {
    BigDecimal getDailyIncome(@Param("todayDate") String todayDate);
}
