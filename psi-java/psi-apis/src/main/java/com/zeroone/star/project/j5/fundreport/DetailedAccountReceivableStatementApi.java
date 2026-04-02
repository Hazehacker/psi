package com.zeroone.star.project.j5.fundreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountReceivableStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountReceivableStatementQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 应收款明细表Api
 */
public interface DetailedAccountReceivableStatementApi {

    /**
     * 查询应收款明细表
     * @param query 查询参数
     * @return 应收款明细列表
     */
    JsonVO<PageDTO<DetailedAccountReceivableStatementDTO>> listDetailedAccountReceivableStatement(DetailedAccountReceivableStatementQuery query);



    /**
     * 导出应收款明细表
     * @param query 查询参数
     * @return 应收款明细列表
     */
    ResponseEntity<byte[]> exportDetailedAccountReceivableStatementToExcel(DetailedAccountReceivableStatementQuery query);

}
