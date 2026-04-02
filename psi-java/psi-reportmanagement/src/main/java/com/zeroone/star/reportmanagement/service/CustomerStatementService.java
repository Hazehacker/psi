package com.zeroone.star.reportmanagement.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.fundreport.CustomerStatementMainDTO;
import com.zeroone.star.project.query.j5.fundreport.CustomerStatementQuery;

import java.util.List;

/**
 * 客户对账单业务逻辑
 *
 * @author toexpl
 * @since 2025/10/27
 */

public interface CustomerStatementService {

    /**
     * 客户对账单表查询
     * @param query
     * @return
     */
    Page<CustomerStatementMainDTO> listCustomerStatement(CustomerStatementQuery query);


    /**
     * 客户对账单信息导出
     * @param query
     */
    List<CustomerStatementMainDTO> exportCustomerStatement(CustomerStatementQuery query);

}
