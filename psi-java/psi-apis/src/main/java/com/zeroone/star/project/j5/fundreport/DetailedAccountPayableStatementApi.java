package com.zeroone.star.project.j5.fundreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.CashBankStatementDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountPayableStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountPayableStatementQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 应付款明细表Api
 */
public interface DetailedAccountPayableStatementApi {

    /**
     * 查询应付款明细表
     * @param query 查询参数
     * @return 应付款明细列表
     */
    JsonVO<PageDTO<DetailedAccountPayableStatementDTO>> listDetailedAccountPayableStatement(DetailedAccountPayableStatementQuery query);



    /**
     * 导出应付款明细表
     * @param query 查询参数
     * @return 应付款明细列表
     */
    ResponseEntity<byte[]> exportDetailedAccountPayableStatementToExcel(DetailedAccountPayableStatementQuery query);

}
