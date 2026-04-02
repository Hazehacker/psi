package com.zeroone.star.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.finance.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 资金账户 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-10-31
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {


    @Select("SELECT  * FROM account WHERE name = #{name}")
    Account  existsByName(String name);
}
