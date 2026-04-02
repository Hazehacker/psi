package com.zeroone.star.sale.service;

import com.zeroone.star.sale.entity.AccountInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 资金详情 服务类
 * </p>
 *
 * @author author
 * @since 2025-10-26
 */
public interface IAccountInfoplusService extends IService<AccountInfo> {

    void saveinfo(String account, String id, BigDecimal money,Integer direction);
}
