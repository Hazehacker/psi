package com.zeroone.star.project.j8.report;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.query.j8.report.StockDetailReportQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j8.report.StockDetailReportVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 商品收发明细报表接口
 * - 获取报表（支持条件查询 + 分页）
 * - 导出数据（Excel / CSV）
 */
public interface StockDetailReportApis {

    /**
     * 获取报表（支持条件查询 + 分页）
     *
     * @param query 查询条件
     * @return 报表数据
     */
    JsonVO<PageDTO<StockDetailReportVO>> getDetailList(StockDetailReportQuery query);

    /**
     * 导出数据（Excel / CSV）
     *@param response response
     * @return 返回下载链接
     */
    void exportDetailData(StockDetailReportQuery stockDetailReportQuery, HttpServletResponse response);
}
