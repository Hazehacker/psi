package com.zeroone.star.reportmanagement.mapper.salesreport;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.query.j5.salesreport.SalesSummaryQuery;
import com.zeroone.star.reportmanagement.entity.SalesSummaryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesSummaryMapper {
    /**
     * 查询销售汇总DO列表
     */
//    List<SalesSummaryDO> selectSalesSummaryDO(@Param("query") SalesSummaryQuery query);
    IPage<SalesSummaryDO> selectSalesSummaryPage(Page<SalesSummaryDO> page, @Param("query") SalesSummaryQuery query);
}
