package com.zeroone.star.finance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.finance.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 供应商 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-10-31
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {



    @Select("SELECT * FROM supplier WHERE name = #{name}")
    Supplier  existsByName(String name);
}
