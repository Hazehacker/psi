package com.zeroone.star.project.j5.fundreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.UnitArrearsReportDTO;
import com.zeroone.star.project.query.j5.fundreport.UnitReceiptQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 往来单位欠款Api
 * @author 言语
 * @date 2025/10/20
 */
public interface UnitArrearsReportApi {

    /**
     * 查询往来单位欠款表
     * @param query 收款查询参数
     * @return 往来单位欠款列表
     */
    JsonVO<PageDTO<UnitArrearsReportDTO>> listUnitArrearsReportForm(UnitReceiptQuery query);

    /**
     * 查询往来单位欠款表
     * @param query 收款查询参数
     * @return 往来单位欠款列表
     */
    ResponseEntity<byte[]> exportUnitArrearsReportFormToExcel(UnitReceiptQuery query);

}
