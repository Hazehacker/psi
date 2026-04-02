package com.zeroone.star.payment_order.service;

import com.zeroone.star.payment_order.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.payment_order.entity.OmyInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 资金账户 服务类
 * </p>
 *
 * @author 温白开
 * @since 2025-10-28
 */
public interface AccountService extends IService<Account> {
    //扣减余额
    void updateBatchBalance(OmyInfo info, String sourceId);
    //恢复余额
    void UpdateBatchBalanceRestore(String omyId);

}
