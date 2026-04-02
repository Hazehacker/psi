package com.zeroone.star.basic_information.service.util;

import com.zeroone.star.basic_information.entity.Account;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.AccountDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.SaveAccountDTO;

/**
 * <p>
 * 描述: 模型转换
 * </p>
 *
 * @author goldenHour
 * @version 1.0.0
 */
public class AccountConverter {

    /**
     * Account 转 AccountDTO
     * @param account
     * @return AccountDTO
     */
    public static AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .number(account.getNumber())
                .frame(account.getFrame())
                .time(account.getTime())
                .initial(account.getInitial())
                .data(account.getData())
                .build();
    }

    /**
     * SaveAccountDTO 转 Account
     * @param saveAccountDTO
     * @return Account
     */

    public static Account toEntity(SaveAccountDTO saveAccountDTO) {
        return Account.builder()
                .name(saveAccountDTO.getName())
                .number(saveAccountDTO.getNumber())
                .frame(saveAccountDTO.getFrame())
                .time(saveAccountDTO.getTime())
                .initial(saveAccountDTO.getInitial())
                .balance(saveAccountDTO.getInitial())// 初始余额即为当前余额
                .build();
    }

    /**
     * AccountDTO 转 Account
     * @param accountDTO
     * @return Account
     */
    public static Account toEntity(AccountDTO accountDTO) {
        return Account.builder()
                .id(accountDTO.getId())
                .name(accountDTO.getName())
                .number(accountDTO.getNumber())
                .frame(accountDTO.getFrame())
                .time(accountDTO.getTime())
                .initial(accountDTO.getInitial())
                .balance(accountDTO.getInitial())// 初始余额即为当前余额
                .build();
    }
}
