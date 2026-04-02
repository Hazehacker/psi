package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.Buy;
import com.zeroone.star.project.dto.j1.homepage.DV;
import com.zeroone.star.project.query.j1.homepage.PurchaseDailyTotalQuery;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * <p>
 * 采购 服务类
 * </p>
 *
 * @author zonk
 * @since 2025-11-1
 */
public interface IBuyService extends IService<Buy>{
    /**
     * 查找销售单统计
     * @param query
     * @return
     */
    ArrayList<DV> getPurchaseOrderStatistics(@Valid PurchaseDailyTotalQuery query);

}
