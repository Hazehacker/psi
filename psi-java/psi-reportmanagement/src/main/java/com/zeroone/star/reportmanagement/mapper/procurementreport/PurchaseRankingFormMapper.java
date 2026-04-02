package com.zeroone.star.reportmanagement.mapper.procurementreport;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseRankingFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseRankingFormQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PurchaseRankingFormMapper {
    IPage<PurchaseRankingFormDTO> listPurchaseRankingForm(
            Page<PurchaseRankingFormDTO> page,
            @Param("query") PurchaseRankingFormQuery query);
}
