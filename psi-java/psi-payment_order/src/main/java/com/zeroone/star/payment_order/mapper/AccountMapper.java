package com.zeroone.star.payment_order.mapper;

import com.zeroone.star.payment_order.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.math.BigDecimal;

/**
 * <p>
 * 资金账户 Mapper 接口
 * </p>
 *
 * @author 温白开
 * @since 2025-10-28
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
    //扣减余额
    @Update("UPDATE account SET balance = balance - #{money} WHERE name = #{name}")
    int updateBalance(@Param("name") String account, @Param("money") BigDecimal money);

    //恢复余额
    @Update("UPDATE account SET balance = balance + #{money} WHERE name = #{name}")
    int updateBalanceAdd(@Param("name") String account, @Param("money") BigDecimal money);


}
