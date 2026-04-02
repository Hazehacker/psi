package com.zeroone.star.project.j5.salesreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesRankingTableDTO;
import com.zeroone.star.project.query.j5.salesreport.NoRequiredSalesRankingTableQuery;
import com.zeroone.star.project.query.j5.salesreport.SalesRankingTableQury;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

public interface SalesRankingTableApi {
    /**
     * 销售排行报表
     * @param query 查询参数
     * @return 销售排行列表
     */
    JsonVO<PageDTO<SalesRankingTableDTO>> listSalesRankingTableDTO(SalesRankingTableQury query);
    /**
     * 导出销售排行报表
     * @param query 查询参数
     * @return 销售排行列表
     */
    ResponseEntity<byte[]> exportListSalesRankingTableToExcel(NoRequiredSalesRankingTableQuery query);
}
