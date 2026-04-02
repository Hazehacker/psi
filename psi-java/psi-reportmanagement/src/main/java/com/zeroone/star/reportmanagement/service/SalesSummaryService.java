package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesSummaryDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesSummaryExportDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesSummaryQuery;
import com.zeroone.star.reportmanagement.entity.SalesSummaryDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* 销售汇总表接口
* */
public interface SalesSummaryService {
    /*
     * 分类分页查询
     * */
    PageDTO<SalesSummaryDTO> query(SalesSummaryQuery query);

    /*
    * 查询所有数据不分页
    * */
    List<SalesSummaryExportDTO> queryForExport(SalesSummaryQuery query);
}
