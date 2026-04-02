package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.Omy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j1.homepage.OmyDailyTotalDto;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 付款单 服务类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
public interface IOmyService extends IService<Omy> {
    List<OmyDailyTotalDto> listOmyDailyTotal(LocalDate from,LocalDate to);
}
