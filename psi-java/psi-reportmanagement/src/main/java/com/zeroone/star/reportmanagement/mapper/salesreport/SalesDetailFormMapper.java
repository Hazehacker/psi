package com.zeroone.star.reportmanagement.mapper.salesreport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesDetailFormDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesDetailFormQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售明细表Mapper
 * @author rainsilent
 * @date 2025/10/28
 */
public interface SalesDetailFormMapper {
    Page<SalesDetailFormDTO> selectSalesDetailForm(Page<SalesDetailFormDTO> page,
                                                      @Param("query") SalesDetailFormQuery query);

    List<SalesDetailFormDTO> selectAllForExcel(@Param("query") SalesDetailFormQuery query);
}
