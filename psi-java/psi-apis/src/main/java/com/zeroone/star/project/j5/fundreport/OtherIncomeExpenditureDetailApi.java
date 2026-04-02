package com.zeroone.star.project.j5.fundreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountPayableStatementDTO;
import com.zeroone.star.project.dto.j5.fundreport.OtherIncomeExpenditureDetailDTO;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountPayableStatementQuery;
import com.zeroone.star.project.query.j5.fundreport.OtherIncomeExpenditureQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 其他收支明细表Api
 */
public interface OtherIncomeExpenditureDetailApi {

    /**
     * 查询其他收支明细表
     * @param query 查询参数
     * @return 其他收支明细列表
     */
    JsonVO<PageDTO<OtherIncomeExpenditureDetailDTO>> listOtherIncomeExpenditureDetail(OtherIncomeExpenditureQuery query);



    /**
     * 导出其他收支明细表
     * @param query 查询参数
     * @return 其他收支明细列表
     */
    ResponseEntity<byte[]> exportOtherIncomeExpenditureDetailToExcel(OtherIncomeExpenditureQuery query);

}
