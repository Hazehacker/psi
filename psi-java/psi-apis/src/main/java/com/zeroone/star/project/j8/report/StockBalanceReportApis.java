package com.zeroone.star.project.j8.report;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.report.ProductStockBalanceDTO;
import com.zeroone.star.project.query.j8.report.ProductStockBalanceQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

/**
 * 商品库存余额报表接口
 * 获取全部
 * 新增
 * 删除
 * 修改
 * 查询
 * - 获取报表（支持条件查询 + 分页）
 * - 导出数据（Excel / CSV）
 * autor:冯烨
 */
public interface StockBalanceReportApis {
    JsonVO<PageDTO<ProductStockBalanceDTO>> getProductStocksByCondition(ProductStockBalanceQuery query);

    ResponseEntity<byte[]> export(ProductStockBalanceQuery query);
}
