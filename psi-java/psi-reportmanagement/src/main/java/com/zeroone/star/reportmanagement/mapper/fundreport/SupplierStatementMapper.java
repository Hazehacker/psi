package com.zeroone.star.reportmanagement.mapper.fundreport;


import com.zeroone.star.project.dto.j5.fundreport.SupplierStatementMainDTO;
import com.zeroone.star.project.query.j5.fundreport.SupplierStatementQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 供应商对账表持久层实现
 *
 * @author toexpl
 * @since 2025/10/27
 */

@Mapper
public interface SupplierStatementMapper {

    /**
     * 查询供应商对账单
     * @param query
     */
    List<SupplierStatementMainDTO> selectSupplierStatementMain(SupplierStatementQuery query);
}
