package com.zeroone.star.reportmanagement.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.fundreport.SupplierStatementMainDTO;
import com.zeroone.star.project.query.j5.fundreport.SupplierStatementQuery;

import java.util.List;

/**
 * 供应商对账单
 *
 * @author toexpl
 * @since 2025/10/28
 */

public interface SupplierStatementService {

    /**
     * 供应商对账单查询
     * @param query
     * @return
     */
    Page<SupplierStatementMainDTO> listSupplierStatement(SupplierStatementQuery query);

    /**
     * 供应商对账单导出
     * @param query
     * @return
     */
    List<SupplierStatementMainDTO> exportSupplierStatement(SupplierStatementQuery query);
}
