package com.zeroone.star.homepage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.homepage.entity.Sre;
import com.zeroone.star.homepage.mapper.SreMapper;
import com.zeroone.star.homepage.service.ISreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j1.homepage.SreCountsDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 销售退货单 服务实现类
 * </p>
 *
 * @author MRME39
 * @since 2025-10-23
 */
@Service
public class SreServiceImpl extends ServiceImpl<SreMapper, Sre> implements ISreService {

    @Resource
    SreMapper sreMapper;

    /**
     * 从数据库中获取数据并且统计同一天的总和金额
     * @return 同一天的总和金额
     */
    @Override
    public List<SreCountsDTO> Srecounts() {
        //获取数据
        List<SreCountsDTO> sreCountsDTOS = sreMapper.SreCount();
        Map<LocalDate, BigDecimal> map = new HashMap<>();
        // 按日期汇总金额
        for(SreCountsDTO x : sreCountsDTOS){
            LocalDate date = x.getDate();
            BigDecimal decimal =  x.getValue();
            map.put(date,map.getOrDefault(date,BigDecimal.ZERO).add(decimal));
        }
        // 将Map转换回List<SreCountsDTO>
        List<SreCountsDTO> result = new ArrayList<>();
        for(Map.Entry<LocalDate, BigDecimal> e : map.entrySet()) {
            SreCountsDTO dto = new SreCountsDTO(e.getKey(),e.getValue());
            result.add(dto);
        }
        return result;
    }
}
