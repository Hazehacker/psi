package com.zeroone.star.payment_order.mapper;

import com.zeroone.star.payment_order.entity.Supplier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * <p>
 * 供应商 Mapper 接口
 * </p>
 *
 * @author 温白开
 * @since 2025-10-28
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
        //减少应付款
        @Update("UPDATE supplier SET balance = balance - #{money} WHERE name = #{name}")
        int updatePayable(@Param("name") String supplierId, @Param("money") BigDecimal money);

        //恢复应付款
        @Update("UPDATE supplier SET balance = balance + #{money} WHERE name = #{name}")
        int updatePayableAdd(@Param("name") String supplierId, @Param("money") BigDecimal money);

}