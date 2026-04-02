package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.vo.j1.homepage.AccountBalanceVO;
import com.zeroone.star.project.vo.j1.homepage.BalanceVO;
import com.zeroone.star.project.vo.j1.homepage.ListVO;

/**
 * <p>
 * 资金账户 服务类
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */

public interface IAccountService extends IService<Account> {

    BalanceVO queryBalance();

    ListVO<AccountBalanceVO> queryAccountBalance();
}
