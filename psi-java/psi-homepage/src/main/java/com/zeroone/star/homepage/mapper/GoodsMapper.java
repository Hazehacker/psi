package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select sum((sell-buy) * stock) from goods")
    BigDecimal queryBalanceSum();
}
