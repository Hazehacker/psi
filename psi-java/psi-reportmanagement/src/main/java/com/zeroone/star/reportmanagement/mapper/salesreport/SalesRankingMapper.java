package com.zeroone.star.reportmanagement.mapper.salesreport;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.salesreport.SalesRankingTableDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesRankingTableQury;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesRankingMapper {

    Page<SalesRankingTableDTO> getSalesRankingList(Page<SalesRankingTableDTO> page,
                                                   @Param("query")SalesRankingTableQury qury);


//    List<SalesRankingTableDTO> getSalesRankingListNoPage(@Param("query") SalesRankingTableQury query);


    List<SalesRankingTableDTO> getSalesRankingListNoPage(@Param("query")SalesRankingTableQury qury);
}
