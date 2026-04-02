package com.zeroone.star.homepage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.homepage.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 * 供应商 Mapper 接口
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {

    @Select("select sum(balance) from supplier")
    BigDecimal queryBalanceSum();
}
