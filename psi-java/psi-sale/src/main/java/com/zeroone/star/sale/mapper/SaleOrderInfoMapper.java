package com.zeroone.star.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j4.sale.SaleOrderInfoDTO;
import com.zeroone.star.sale.entity.SaleOrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SaleOrderInfoMapper extends BaseMapper<SaleOrderInfo> {
    /**
     * 根据id查询销售订单详情
     *
     * @param id 销售订单id
     * @return 销售订单详情
     */
    @Select("select * from sor_info where pid = #{id}")
    List<SaleOrderInfoDTO> getById(String id);


}
