package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.vo.j1.homepage.AccountBalanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 资金账户 Mapper 接口
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

    @Select("select sum(balance) from account")
    BigDecimal queryBalanceSum();

    @Select("select name,balance as value from account")
    List<AccountBalanceVO> queryAccountBalance();
}
