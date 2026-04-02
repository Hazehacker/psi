package com.zeroone.star.reportmanagement.service.salesreport;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesRankingTableDTO;

import com.zeroone.star.project.query.j5.salesreport.SalesRankingTableQury;


public interface SalesRankingTableService {

    PageDTO<SalesRankingTableDTO> listSalesRankingTable(SalesRankingTableQury query);

}
