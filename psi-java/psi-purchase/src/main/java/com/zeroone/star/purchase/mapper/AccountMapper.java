package com.zeroone.star.purchase.mapper;

import com.zeroone.star.purchase.DO.AccountDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 资金账户 Mapper 接口
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Mapper
public interface AccountMapper extends BaseMapper<AccountDO> {
    int updateAccountByName(@Param("name") String name,
                            @Param("balance") BigDecimal balance);

}
