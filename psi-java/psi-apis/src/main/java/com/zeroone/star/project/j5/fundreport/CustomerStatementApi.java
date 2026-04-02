package com.zeroone.star.project.j5.fundreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.CustomerStatementMainDTO;
import com.zeroone.star.project.query.j5.fundreport.CustomerStatementQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 客户对账单表操作接口
 * @author toexpl
 * @since  2025-10-20
 */
public interface CustomerStatementApi {

    /**
     * 查询客户对账单
     * @param query
     * @return
     */
    JsonVO<PageDTO<CustomerStatementMainDTO>> customerStatementQueryBy(CustomerStatementQuery query);

    /**
     * 客户对账单导出操作
     * @param query
     * @return
     */
    ResponseEntity<byte[]> customerStatementExport(CustomerStatementQuery query);
}
