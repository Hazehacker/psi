package com.zeroone.star.homepage.service.impl;

import com.zeroone.star.homepage.entity.Sell;
import com.zeroone.star.homepage.mapper.SellMapper;
import com.zeroone.star.homepage.service.ISellService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j1.homepage.SalesDailyTotalDTO;
import com.zeroone.star.project.dto.j1.homepage.SalesDailyTotalResponseDTO;
import com.zeroone.star.project.query.j1.homepage.SalesDailyTotalQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 销售单 服务实现类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Service
public class SellServiceImpl extends ServiceImpl<SellMapper, Sell> implements ISellService {

    @Resource
    SellMapper sellMapper;

    /**
     * 查询每日销售单汇总
     * @param query
     * @return
     */
    @Override
    public SalesDailyTotalResponseDTO querySalesDailyTotal(SalesDailyTotalQuery query) {
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        if (startDate == null && endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(30);
        } else if (startDate == null) {
            startDate = endDate.minusDays(30);
        } else if (endDate == null) {
            endDate = startDate.plusDays(30);
        }

        String startDateString = startDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDateString = endDate.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<SalesDailyTotalDTO> salesDailyTotalDTOS = sellMapper.selectSalesDailyTotal(startDateString, endDateString);
        SalesDailyTotalResponseDTO salesDailyTotalResponseDTO = new SalesDailyTotalResponseDTO(salesDailyTotalDTOS);

        return salesDailyTotalResponseDTO;
    }
}
