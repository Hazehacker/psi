package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.SellInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j1.homepage.DataStatisticsDTO;

import java.math.BigDecimal;

/**
 * <p>
 * 销售单详情 服务类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
public interface ISellInfoService extends IService<SellInfo> {
    /**
     * 获取数据统计
     * @return 相应数据
     */
    DataStatisticsDTO getDataStatisticsDTO();


}
