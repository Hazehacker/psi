package com.zeroone.star.payment_order.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.payment_order.entity.Omy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.payment_order.entity.OmyBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 付款单 Mapper 接口
 * </p>
 *
 * @author 温白开
 * @since 2025-10-24
 */
@Mapper
public interface OmyMapper extends BaseMapper<Omy> {
    /*
    * 更新审核状态
    * */
    @Update("UPDATE omy SET examine = #{examine} WHERE id = #{id}")
    int updateExamineStatus(@Param("id") String id, @Param("examine") Integer examine);


}
