package com.zeroone.star.payment_order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.payment_order.entity.OmyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 付款单Mapper接口
 */
@Mapper
//继承了BaseMapper就不用写xml了
public interface OmyMapper2 extends BaseMapper<OmyDO> {
}