package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 * 客户 Mapper 接口
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    @Select("select sum(balance) from customer")
    BigDecimal queryBalanceSum();
}
