package com.zeroone.star.reportmanagement.mapper.salesreport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j5.salesreport.SalesProfitDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesProfitDetailedDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SalesProfitMapper extends BaseMapper<Object> {

    Page<SalesProfitDTO> selectSalesProfit(@Param("page") Page<Object> page, @Param("query")SalesQuery query);

    Page<SalesProfitDetailedDTO> listSalesDetailedProfit(@Param("page") Page<Object> page, @Param("query") SalesQuery query);

}
