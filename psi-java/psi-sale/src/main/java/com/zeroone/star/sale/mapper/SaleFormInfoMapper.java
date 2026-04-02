package com.zeroone.star.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j4.sale.SaleDetailDTO;
import com.zeroone.star.sale.entity.SaleFormInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SaleFormInfoMapper extends BaseMapper<SaleFormInfo> {

    /**
     * 根据条件查询销售单详情
     */
    @Select("<script>" +
            "SELECT sfi.* FROM sell_info sfi " +
            "JOIN sell sf ON sfi.pid = sf.id " +
            "<where>" +
            "   <if test='customer != null and customer != \"\"'>" +
            "       AND sf.customer = #{customer}" +
            "   </if>" +
            "   <if test='time != null'>" +
            "       AND sf.time = #{time}" +
            "   </if>" +
            "   <if test='number != null and number != \"\"'>" +
            "       AND sf.number = #{number}" +
            "   </if>" +
            "</where>" +
            "</script>")
    List<SaleDetailDTO> selectSaleDetailByCondition(@Param("customer") String customer,
                                                    @Param("time") LocalDateTime time,
                                                    @Param("number") String number);
}