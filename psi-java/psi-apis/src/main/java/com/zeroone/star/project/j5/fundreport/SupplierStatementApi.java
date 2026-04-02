package com.zeroone.star.project.j5.fundreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.SupplierStatementMainDTO;
import com.zeroone.star.project.query.j5.fundreport.SupplierStatementQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

import java.io.OutputStream;

/**
 * 客户对账单表操作接口
 * @author toexpl
 * @since  2025-10-20
 */
public interface SupplierStatementApi {

    /**
     * 供应商对账单查询
     * @param query
     * @return
     */
    JsonVO<PageDTO<SupplierStatementMainDTO>> supplierStatementQueryBy(SupplierStatementQuery query);

    /**
     * 供应商对账单导出
     * @param query
     * @return
     */
    ResponseEntity<byte[]> supplierStatementExport(SupplierStatementQuery query);
}
