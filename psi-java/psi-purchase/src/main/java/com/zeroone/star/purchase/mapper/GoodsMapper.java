package com.zeroone.star.purchase.mapper;

import com.zeroone.star.purchase.DO.GoodsDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
@Mapper
@Component("PurchaseGoodsMapper")
public interface GoodsMapper extends BaseMapper<GoodsDO> {

}
