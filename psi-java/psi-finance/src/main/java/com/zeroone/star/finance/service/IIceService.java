package com.zeroone.star.finance.service;

import com.zeroone.star.finance.entity.Ice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.otherincome.AddOtherIncomeDTO;
import com.zeroone.star.project.dto.j8.finance.otherincome.OtherIncomeDTO;
import com.zeroone.star.project.dto.j8.finance.otherincome.OtherIncomeDetailDTO;
import com.zeroone.star.project.query.j8.finance.otherincome.OtherIncomeQuery;

/**
 * <p>
 * 其它收入单 服务类
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
public interface IIceService extends IService<Ice> {
    /**
     * 获取收入单列表（分页）
     */
    PageDTO<OtherIncomeDTO> listIncomeOrders(OtherIncomeQuery query);

    /**
     * 获取指定收入单详情
     */
    OtherIncomeDetailDTO getIncomeOrder(String id);

    /**
     * 新增收入单
     */
    String addIncomeOrder(AddOtherIncomeDTO dto);
}
