package com.zeroone.star.basic_information.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.basic_information.entity.Account;
import com.zeroone.star.project.query.j6.basic_information.fund_account.AccountQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 描述: 资金账户Mapper接口
 * </p>
 *
 * @author goldenHour
 * @version 1.0.0
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

}
