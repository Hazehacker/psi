package com.zeroone.star.project.j5.procurementreport;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.ProcurementDetailFromDTO;
import com.zeroone.star.project.query.j5.procurementreport.ProcurementDetailFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;
import springfox.documentation.spring.web.json.Json;

/**
 * <p>
 * 采购明细报表Api
 * </p>
 *
 * @author chuming_7
 * @since 2025-10-21
 */
public interface ProcurementDetailReportApi {
    /**
     * 获取采购明细表
     * @param query 查询参数
     * @return 采购明细表数据
     */
    JsonVO<PageDTO<ProcurementDetailFromDTO>> listProcurementDetailForm(ProcurementDetailFormQuery query);

    /**
     * 导出采购明细表
     * @param query 查询参数
     * @return 采购明细表数据
     */
    ResponseEntity<byte[]> exportProcurementDetailFormToExcel(ProcurementDetailFormQuery query);
}
