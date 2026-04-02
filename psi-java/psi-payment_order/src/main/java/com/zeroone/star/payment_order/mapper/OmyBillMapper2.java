package com.zeroone.star.payment_order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.payment_order.entity.OmyBillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface OmyBillMapper2 extends BaseMapper<OmyBillDO> {
    /**
     * 根据付款单主表id，查询该单据的核销金额总和
     */
    @Select("SELECT SUM(money) FROM omy_bill WHERE pid = #{pid}")
    BigDecimal sumMoneyByPid(String pid);
}
