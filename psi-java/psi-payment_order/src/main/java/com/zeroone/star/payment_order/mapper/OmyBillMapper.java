package com.zeroone.star.payment_order.mapper;

import com.zeroone.star.payment_order.entity.OmyBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 付款单核销详情 Mapper 接口
 * </p>
 *
 * @author 温白开
 * @since 2025-10-24
 */
@Mapper
public interface OmyBillMapper extends BaseMapper<OmyBill> {


}
