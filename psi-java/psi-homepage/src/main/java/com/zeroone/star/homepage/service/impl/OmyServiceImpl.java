package com.zeroone.star.homepage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.homepage.entity.Omy;
import com.zeroone.star.homepage.mapper.OmyMapper;
import com.zeroone.star.homepage.service.IOmyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j1.homepage.OmyDailyTotalDto;
import com.zeroone.star.homepage.utils.localDate.LocalDateUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 付款单 服务实现类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Service
public class OmyServiceImpl extends ServiceImpl<OmyMapper, Omy> implements IOmyService {
    @Override
    public List<OmyDailyTotalDto> listOmyDailyTotal(LocalDate from, LocalDate to) {
        // 查询数据
        LocalDateTime source = LocalDateTime.of(from, LocalTime.MIN);
        LocalDateTime dest = LocalDateTime.of(to,LocalTime.MAX);
        QueryWrapper<Omy> wrapper = new QueryWrapper<>();
        wrapper.select("time,SUM(total) AS total")
                .between("time", source,dest)
                .eq("examine",1)
                .groupBy("time");
        List<Omy> omys =  getBaseMapper().selectList(wrapper);
        // 数据统计
        int nums = LocalDateUtil.countDays(from,to);
        List<OmyDailyTotalDto> result = new ArrayList<>(nums);
        for (int i = 0; i < nums; i++) {
            LocalDate date = from.plusDays(i);
            result.add(new OmyDailyTotalDto(date,BigDecimal.ZERO));
        }
        omys.forEach(omy -> {
            int index = LocalDateUtil.countDays(from,omy.getTime().toLocalDate())-1;
            OmyDailyTotalDto dto = result.get(index);
            dto.setValue(dto.getValue().add(omy.getTotal()));
        });
        return result;
    }
}
