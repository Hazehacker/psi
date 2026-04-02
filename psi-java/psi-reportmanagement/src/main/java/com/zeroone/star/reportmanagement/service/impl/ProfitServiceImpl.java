package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.ProfitReportDTO;
import com.zeroone.star.project.dto.j5.fundreport.ProfitReportExcelDTO;
import com.zeroone.star.project.query.j5.fundreport.ProfitQuery;
import com.zeroone.star.reportmanagement.mapper.fundreport.ProfitMapper;
import com.zeroone.star.reportmanagement.service.ProfitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfitServiceImpl implements ProfitService {
    private final ProfitMapper profitMapper;
    private final EasyExcelComponent easyExcelComponent;

    @Override
    public PageDTO<ProfitReportDTO> listProfitReportForm(ProfitQuery query) {
        // 看起来是分页但其实形式是固定的:
        // | 主营业务      |              |
        //| ------------- | ------------ |
        //| \| - 销售收入 | 762909122.23 |
        //| \| - 业务成本 | 48           |
        //| \| - 销售成本 | 9436856.93   |
        //|               |              |
        //| 利润          | 753472217.3  |

        // 查 sell 总和
        Map<String, BigDecimal> sell = profitMapper.getSellSummary(query);
        BigDecimal sellActual = sell.get("actualSum");
        BigDecimal sellCost = sell.get("costSum");

        // 查 sre 总和
        Map<String, BigDecimal> sre = profitMapper.getSreSummary(query);
        BigDecimal sreActual = sre.get("actualSum");
        BigDecimal sreCost = sre.get("costSum");

        // 查 sell 和 sre 的所有 id, 用于查 summary
        List<String> sellIds = profitMapper.getSellIds(query);
        List<String> sreIds = profitMapper.getSreIds(query);

        // 查询销售出库和退货入库的bct，在业务层计算销售成本，避免空列表导致的SQL语法错误
        BigDecimal sellBct = sellIds.isEmpty() ? BigDecimal.ZERO : profitMapper.getSellBctSum(sellIds);
        BigDecimal sreBct = sreIds.isEmpty() ? BigDecimal.ZERO : profitMapper.getSreBctSum(sreIds);

        // 计算最终结果
        // 销售收入 = 销售出库实际金额总和 - 退货入库实际金额总和
        BigDecimal salesIncome = sellActual.subtract(sreActual);
        // 业务成本 = 销售出库成本总和 - 退货入库成本总和
        BigDecimal businessCost = sellCost.subtract(sreCost);
        // 销售成本 = 销售出库bct - 退货入库bct
        BigDecimal cogsCost = sellBct.subtract(sreBct);
        // 利润 = 销售收入 - 业务成本 - 销售成本
        BigDecimal profit = salesIncome.subtract(businessCost).subtract(cogsCost);

        Page<ProfitReportDTO> page = new Page<>(1, 6);
        List<ProfitReportDTO> records = new ArrayList<>();
        records.add(new ProfitReportDTO("主营业务", ""));
        records.add(new ProfitReportDTO("| - 销售收入", salesIncome.toPlainString()));
        records.add(new ProfitReportDTO("| - 业务成本", businessCost.toPlainString()));
        records.add(new ProfitReportDTO("| - 销售成本", cogsCost.toPlainString()));
        records.add(new ProfitReportDTO("", ""));
        records.add(new ProfitReportDTO("利润", profit.toPlainString()));
        page.setRecords(records);
        page.setTotal(records.size());
        return PageDTO.create(page);
    }

    @Override
    public byte[] exportProfitReportToExcel(ProfitQuery query) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 获取利润表数据
            PageDTO<ProfitReportDTO> pageDTO = listProfitReportForm(query);
            List<ProfitReportDTO> rows = pageDTO.getRows();

            // 转换为Excel DTO
            List<ProfitReportExcelDTO> exportData = new ArrayList<>();
            for (ProfitReportDTO dto : rows) {
                ProfitReportExcelDTO excelDTO = convertToExcelDTO(dto);
                exportData.add(excelDTO);
            }

            // 导出数据到Excel
            easyExcelComponent.export("利润表", outputStream, ProfitReportExcelDTO.class, exportData);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("导出利润表Excel失败", e);
            throw new RuntimeException("导出Excel失败");
        }
    }

    /**
     * 将ProfitReportDTO转换为ProfitReportExcelDTO
     */
    private ProfitReportExcelDTO convertToExcelDTO(ProfitReportDTO dto) {
        ProfitReportExcelDTO excelDTO = new ProfitReportExcelDTO();
        excelDTO.setProjectName(dto.getProjectName());
        excelDTO.setAmount(dto.getAmount());
        return excelDTO;
    }
}
