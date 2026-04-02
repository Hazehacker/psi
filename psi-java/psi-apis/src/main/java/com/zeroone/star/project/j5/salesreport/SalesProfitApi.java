package com.zeroone.star.project.j5.salesreport;


import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesProfitDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * 销售利润Api
 * @author 言语
 * @date 2025/10/20
 */
public interface SalesProfitApi {

    /**
     * 销售利润表
     * @param query 查询参数
     * @return 往来单位欠款列表
     */
    JsonVO<PageDTO<Object>> listSalesProfitForm(SalesQuery query);

    /**
     * 销售利润表
     * @param query 查询参数
     * @return 往来单位欠款列表
     */
    ResponseEntity<byte[]> exportSalesProfitFormToExcel(SalesQuery query) throws IOException;

}
