package com.zeroone.star.sale.service;

import com.zeroone.star.project.dto.j4.sale.SaleOrderInfoDTO;
import com.zeroone.star.project.vo.JsonVO;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleOrderInfoService {

    /**
     * 获取指定销售订单详情
     *
     * @param customer 客户
     * @param time     时间
     * @param number   单据编号
     * @return 详情
     */
    JsonVO<List<List<SaleOrderInfoDTO>>> getByQuery(String customer, LocalDateTime time, String number);
}
