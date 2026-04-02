package com.zeroone.star.sale.service.impl;

import com.zeroone.star.sale.entity.AccountInfo;
import com.zeroone.star.sale.mapper.AccountInfoplusMapper;
import com.zeroone.star.sale.service.IAccountInfoplusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 资金详情 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-10-26
 */
@Service
public class AccountInfoplusServiceImpl extends ServiceImpl<AccountInfoplusMapper, AccountInfo> implements IAccountInfoplusService {

    @Override
    public void saveinfo(String account, String id, BigDecimal money,Integer direction) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setPid(account);
        accountInfo.setType("sre");
        accountInfo.setClassName(id);
        accountInfo.setTime(LocalDateTime.now());
        accountInfo.setDirection(direction);
        accountInfo.setMoney(money);
        this.save(accountInfo);
    }
}
