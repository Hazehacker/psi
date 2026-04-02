package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.Sell;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j1.homepage.SalesDailyTotalResponseDTO;
import com.zeroone.star.project.query.j1.homepage.SalesDailyTotalQuery;

/**
 * <p>
 * 销售单 服务类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
public interface ISellService extends IService<Sell> {

    /**
     * 查询每日销售单汇总
     * @param query
     * @return
     */
    SalesDailyTotalResponseDTO querySalesDailyTotal(SalesDailyTotalQuery query);
}
