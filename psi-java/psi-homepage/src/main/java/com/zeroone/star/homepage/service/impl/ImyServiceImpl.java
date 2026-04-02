package com.zeroone.star.homepage.service.impl;

import com.zeroone.star.homepage.entity.Imy;
import com.zeroone.star.homepage.mapper.ImyMapper;
import com.zeroone.star.homepage.service.IImyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j1.homepage.ImyCountsDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收款单 服务实现类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Service
public class ImyServiceImpl extends ServiceImpl<ImyMapper, Imy> implements IImyService {
    @Resource
    ImyMapper imyMapper;

    /**
     * 从数据库中获取数据并且统计同一天的总和金额
     * @return 同一天的总和金额
     */
    @Override
    public List<ImyCountsDTO> Imycounts() {
        //获取数据
        List<ImyCountsDTO> imyCountsDTOS = imyMapper.ImyCount();
        Map<LocalDate, BigDecimal> map = new HashMap<>();
        // 按日期汇总金额
        for(ImyCountsDTO x : imyCountsDTOS){
            LocalDate date = x.getDate();
            BigDecimal decimal =  x.getValue();
            map.put(date,map.getOrDefault(date,BigDecimal.ZERO).add(decimal));
        }
        // 将Map转换回List<SreCountsDTO>
        List<ImyCountsDTO> result = new ArrayList<>();
        for(Map.Entry<LocalDate, BigDecimal> e : map.entrySet()) {
            ImyCountsDTO dto = new ImyCountsDTO(e.getKey(),e.getValue());
            result.add(dto);
        }
        return result;
    }
}
