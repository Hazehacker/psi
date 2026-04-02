package com.zeroone.star.project.j5.fundreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.CashBankStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.CashBankStatementQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 现金银行报表Api
 */
public interface CashBankStatementApi {

    /**
     * 查询现金银行报表
     * @param query 查询参数
     * @return 现金银行列表
     */
    JsonVO<PageDTO<CashBankStatementDTO>> listCashBankStatement(CashBankStatementQuery query);

    /**
     * 导出现金银行报表
     * @param query 查询参数
     * @return 现金银行列表
     */
    ResponseEntity<byte[]> exportCashBankStatementToExcel(CashBankStatementQuery query);

}
