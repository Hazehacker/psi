package com.zeroone.star.report.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.query.j8.report.StockDetailReportQuery;
import com.zeroone.star.project.vo.j8.report.StockDetailReportVO;
import com.zeroone.star.report.mapper.StockDetailReportMapper;
import com.zeroone.star.report.service.StockDetailReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class StockDetailReportServiceImpl implements StockDetailReportService {

    private final StockDetailReportMapper stockDetailReportMapper;

    public StockDetailReportServiceImpl(StockDetailReportMapper stockDetailReportMapper) {
        this.stockDetailReportMapper = stockDetailReportMapper;
    }

    @Override
    public PageDTO<StockDetailReportVO> listStockDetail(StockDetailReportQuery query) {
        Page<StockDetailReportVO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        Page<StockDetailReportVO> result = stockDetailReportMapper.pageQuery(page, query);
        return PageDTO.create(result);
    }

    @Override
    public void exportDetailData(StockDetailReportQuery query, HttpServletResponse response) {
        // 1) 查全部（不分页）
        List<StockDetailReportVO> rows = stockDetailReportMapper.listAll(query);

        // 2) 组装表头（按你截图顺序）
        List<List<String>> head = Arrays.asList(
                Collections.singletonList("单据类型"),
                Collections.singletonList("所属组织"),
                Collections.singletonList("往来单位"),
                Collections.singletonList("单据时间"),
                Collections.singletonList("单据编号"),
                Collections.singletonList("商品名称"),
                Collections.singletonList("辅助属性"),
                Collections.singletonList("仓库"),
                Collections.singletonList("单位"),
                Collections.singletonList("入库成本"),
                Collections.singletonList("入库数量"),
                Collections.singletonList("入库总成本"),
                Collections.singletonList("出库成本"),
                Collections.singletonList("出库数量"),
                Collections.singletonList("出库总成本"),
                Collections.singletonList("结存成本"),
                Collections.singletonList("结存数量"),
                Collections.singletonList("结存总成本")
        );

        // 3) 组装数据（保持数值类型为 BigDecimal/Integer，时间转文本）
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<List<Object>> data = new ArrayList<>(rows == null ? 16 : rows.size());
        if (!CollectionUtils.isEmpty(rows)) {
            for (StockDetailReportVO vo : rows) {
                LocalDateTime t = vo.getTime();
                String timeStr = t == null ? "" : dtf.format(t);

                data.add(Arrays.asList(
                        nvl(vo.getTypeName()),   // 单据类型
                        "",                      // 所属组织（当前 VO 无该字段，留空）
                        nvl(vo.getPartnerName()),
                        timeStr,
                        nvl(vo.getNumber()),
                        nvl(vo.getGoodsName()),
                        nvl(vo.getAttr()),
                        nvl(vo.getWarehouseName()),
                        nvl(vo.getUnit()),
                        vo.getInUct(),           // 入库成本
                        vo.getInNums(),          // 入库数量
                        vo.getInBct(),           // 入库总成本
                        vo.getOutUct(),          // 出库成本
                        vo.getOutNums(),         // 出库数量
                        vo.getOutBct(),          // 出库总成本
                        vo.getBalUct(),          // 结存成本
                        vo.getBalUns(),          // 结存数量
                        vo.getBalBct()           // 结存总成本
                ));
            }
        }

        // 4) 写 Excel（流式）
        try {
            String fileName = URLEncoder.encode("商品收发明细表.xlsx", StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + fileName);

            EasyExcel.write(response.getOutputStream())
                    .head(head)
                    .autoCloseStream(Boolean.TRUE)
                    .sheet("商品收发明细表")
                    .doWrite(data);
        } catch (Exception e) {
            // 这里抛运行时异常由全局异常处理返回友好信息
            throw new RuntimeException("导出失败", e);
        }
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}
