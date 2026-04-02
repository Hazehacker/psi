package com.zeroone.star.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j4.sale.SaleListDTO;
import com.zeroone.star.project.query.j4.sale.SaleQuery;
import com.zeroone.star.sale.entity.SaleForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SaleFormMapper extends BaseMapper<SaleForm> {

    /**
     * 查询销售单分页数据
     */
    Page<SaleListDTO> selectSaleList(Page<SaleListDTO> page, @Param("query") SaleQuery query);

    /**
     * 根据销售单ID查询用于生成退货单的数据
     */
    @Select("SELECT * FROM sell WHERE id = #{saleId}")
    SaleForm selectSaleForReturn(String saleId);
}