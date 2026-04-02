package com.zeroone.star.project.j8.report;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.report.wss_KazamataNeri.StockSummaryReportDTO;
import com.zeroone.star.project.query.j8.report.StockSummaryReportQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.ResponseEntity;

/**
 * 商品收发汇总报表接口
 * - 获取报表（支持条件查询 + 分页）
 * - 导出数据（Excel / CSV）
 */
public interface StockSummaryReportApis {
    /**
     * 查询商品收发汇总表
     *
     * @param stockSummaryReportQuery 条件查询Query
     * @return 查询结果
     */
    @ApiModelProperty("查询商品收发汇总表")
    JsonVO<PageDTO<StockSummaryReportDTO>> listGoodsReceiptAndDispatchSummary(StockSummaryReportQuery stockSummaryReportQuery);

    /**
     * 导出商品收发汇总表报表
     *
     * @param date 导出集合
     * @return 导出表格
     */
    @ApiModelProperty("导出商品收发汇总表报表")
    ResponseEntity<byte[]> exportWssExcel(StockSummaryReportQuery stockSummaryReportQuery);
}
