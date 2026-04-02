package com.zeroone.star.report.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.query.j8.report.StockDetailReportQuery;
import com.zeroone.star.project.vo.j8.report.StockDetailReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockDetailReportMapper {
    Page<StockDetailReportVO> pageQuery(@Param("page") Page<StockDetailReportVO> page, @Param("query") StockDetailReportQuery query);

    List<StockDetailReportVO> listAll(@Param("query") StockDetailReportQuery query);
}
