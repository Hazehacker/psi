package com.zeroone.star.basic_information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.basic_information.entity.Account;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.AccountDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.AccountListDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.SaveAccountDTO;
import com.zeroone.star.project.query.PageQuery;
import com.zeroone.star.project.query.j6.basic_information.fund_account.AccountQuery;

public interface IAccountService extends IService<Account> {

    PageDTO<AccountListDTO> listAccounts(AccountQuery accountQuery, PageQuery pageQuery);

    AccountDTO getAccount(String id);

    int addAccount(SaveAccountDTO saveAccountDTO);

    int updateAccount(AccountDTO AccountDTO);

    int deleteAccount(String id);
}
