package com.zeroone.star.homepage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zeroone.star.homepage.entity.SellInfo;
import com.zeroone.star.homepage.mapper.AccountInfoMapper;
import com.zeroone.star.homepage.mapper.SellInfoMapper;
import com.zeroone.star.homepage.service.ISellInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j1.homepage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.now;

/**
 * <p>
 * 销售单详情 服务实现类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Service
@Slf4j
public class SellInfoServiceImpl extends ServiceImpl<SellInfoMapper, SellInfo> implements ISellInfoService {

    String DataStatisticsRedisKey = "DataStatistics";

    @Resource
    private AccountInfoMapper accountInfoMapper;
    @Resource
    private SellInfoMapper sellInfoMapper;


    @Transactional
    @Override
    public DataStatisticsDTO getDataStatisticsDTO() {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DataStastics todayStatics = getStatics(today);
        String yesterday = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DataStastics yesterdayStatics = getStatics(yesterday);
        // 组装数据
        DataStatisticsDTO dataStatisticsDTO = convertStatisticsDTO(todayStatics,yesterdayStatics);
        return dataStatisticsDTO;
    }

    private DataStastics getStatics(String today){
        // 获取今日 销售额 和 销售量
        DailySalesStats dailySalesStats = sellInfoMapper.selectDailySales(today);
        // 毛利
        BigDecimal dailyGross = sellInfoMapper.getDailyGrossAmount(today);
        // 资金收入
        BigDecimal dailyIncome = accountInfoMapper.getDailyIncome(today);
        // 判空处理
        if(dailySalesStats == null){
            dailySalesStats = new DailySalesStats();
        }

        DataStastics todayStatics = new DataStastics(
                dailySalesStats.getDailySalesQuantity() == null ? 0 : dailySalesStats.getDailySalesQuantity(),
                dailySalesStats.getDailySalesAmount() == null ? BigDecimal.ZERO : dailySalesStats.getDailySalesAmount(),
                dailyGross == null ? BigDecimal.ZERO : sellInfoMapper.getDailyGrossAmount(today),
                dailyIncome == null ? BigDecimal.ZERO : accountInfoMapper.getDailyIncome(today)
        );
        return todayStatics;
    }

    private DataStatisticsDTO convertStatisticsDTO(DataStastics today, DataStastics yesterday){

        // 计算增幅
        int countRate = Integer.parseInt(getRate(today.getCount(),yesterday.getCount()));
        int incomeRate = Integer.parseInt(getRate(today.getIncome().intValue(),yesterday.getIncome().intValue()));
        int grossRate = Integer.parseInt(getRate(today.getGross().intValue(),yesterday.getGross().intValue()));
        int salesRate = Integer.parseInt(getRate(today.getSales().intValue(),yesterday.getSales().intValue()));
        // 组装数据
        DailyCount dailyCount = new DailyCount(today.getCount() == null ? 0: today.getCount(), countRate);
        DailyIncome dailyIncome = new DailyIncome(StringUtils.isNotBlank(today.getIncome().toString())? today.getIncome() : BigDecimal.ZERO, incomeRate);
        DailySales dailySales1 = new DailySales(StringUtils.isNotBlank(today.getSales().toString()) ? today.getSales().intValue() : 0, salesRate);
        DailyGross dailyGross = new DailyGross(StringUtils.isNotBlank(today.getGross().toString()) ? today.getGross().intValue() : 0, grossRate);
        return new DataStatisticsDTO(dailySales1, dailyGross, dailyCount, dailyIncome);
    }

    private String getRate(Integer today, Integer yesterday){
        if(today == 0){
            return "0";
        }
        if(yesterday == 0){
            return "100";
        }
        return String.format("%.0f", today * 100.0 / yesterday);
    }

}
