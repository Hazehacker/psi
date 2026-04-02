package com.zeroone.star.finance.service.impl.tradeexpense;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.finance.entity.TradeExpenseDO;
import com.zeroone.star.finance.mapper.tradeexpense.TradeExpenseMapper;
import com.zeroone.star.finance.service.tradeexpense.TradeExpenseService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.CostDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.ReportInfoDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.TradeExpenseReportDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.TradeExpenseResponseDTO;
import com.zeroone.star.project.query.j8.finance.tradeexpense.TradeExpenseQuery;
import com.zeroone.star.project.query.j8.finance.tradeexpense.TradeExpenseReportQuery;
import lombok.SneakyThrows;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TradeExpenseServiceImpl implements TradeExpenseService {
    @Resource
    private TradeExpenseMapper tradeExpenseMapper;
    @Resource
    private MsTradeExpense msTradeExpense;

    public PageDTO<TradeExpenseResponseDTO> queryTradeExpensePage(TradeExpenseQuery query) {
        Page<TradeExpenseDO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        Page<TradeExpenseDO> tradeExpensePage = tradeExpenseMapper.queryTradeExpensePage(page, query);
        return PageDTO.create(tradeExpensePage, msTradeExpense::TradeExpenseDO2DTO);
    }

    public List<CostDTO> getSettles(List<String> ids) {
        return tradeExpenseMapper.getSettles(ids);
    }

    @SneakyThrows
    public ResponseEntity<byte[]> exportBillExcel(List<String> ids) {
        // 1) 查询
        List<TradeExpenseDO> rows = tradeExpenseMapper.queryTradeExpense(ids);

        // 2) 组装 Excel（纯 Apache POI）
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        org.apache.poi.xssf.usermodel.XSSFWorkbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("购销费用");

        final int COLS = 10; // 总列数
        int r = 0;

        // ============ 样式 ============
        org.apache.poi.ss.usermodel.Font titleFont = wb.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);

        org.apache.poi.ss.usermodel.CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        titleStyle.setFont(titleFont);

        org.apache.poi.ss.usermodel.Font headFont = wb.createFont();
        headFont.setBold(true);

        org.apache.poi.ss.usermodel.CellStyle headStyle = wb.createCellStyle();
        headStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        headStyle.setFont(headFont);
        headStyle.setWrapText(true);

        org.apache.poi.ss.usermodel.CellStyle textStyle = wb.createCellStyle();
        textStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);

        org.apache.poi.ss.usermodel.CellStyle numberStyle = wb.createCellStyle();
        numberStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        // 如需小数格式可启用：
        // org.apache.poi.ss.usermodel.DataFormat df = wb.createDataFormat();
        // numberStyle.setDataFormat(df.getFormat("0.####"));

        // 3) 标题（合并 A1~J1）
        org.apache.poi.ss.usermodel.Row titleRow = sheet.createRow(r++);
        titleRow.setHeightInPoints(22);
        org.apache.poi.ss.usermodel.Cell t0 = titleRow.createCell(0);
        t0.setCellValue("购销费用");
        t0.setCellStyle(titleStyle);
        // 其余空单元格也设置样式，避免合并后显示不一致
        for (int c = 1; c < COLS; c++) {
            org.apache.poi.ss.usermodel.Cell tc = titleRow.createCell(c);
            tc.setCellStyle(titleStyle);
        }
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, COLS - 1));

        // 4) 表头
        String[] header = {
                "单据类型", "所属组织", "往来单位", "单据时间", "单据编号",
                "支出类别", "结算状态", "金额", "已结算金额", "未结算金额"
        };
        org.apache.poi.ss.usermodel.Row headRow = sheet.createRow(r++);
        for (int c = 0; c < header.length; c++) {
            org.apache.poi.ss.usermodel.Cell cell = headRow.createCell(c);
            cell.setCellValue(header[c]);
            cell.setCellStyle(headStyle);
        }

        // 5) 数据
        java.math.BigDecimal sumAmount = java.math.BigDecimal.ZERO;
        java.math.BigDecimal sumSettled = java.math.BigDecimal.ZERO;
        java.math.BigDecimal sumUnsettled = java.math.BigDecimal.ZERO;

        for (TradeExpenseDO e : rows) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(r++);

            // 0 单据类型（DO 已有字段：documentType）
            org.apache.poi.ss.usermodel.Cell c0 = row.createCell(0);
            c0.setCellValue(e.getDocumentType());
            c0.setCellStyle(textStyle);


            // 1 所属组织
            org.apache.poi.ss.usermodel.Cell c1 = row.createCell(1);
            c1.setCellValue(e.getOrganization() == null ? "默认组织" : e.getOrganization());
            c1.setCellStyle(textStyle);

            // 2 往来单位
            org.apache.poi.ss.usermodel.Cell c2 = row.createCell(2);
            c2.setCellValue(e.getPartnerUnit() == null ? "" : e.getPartnerUnit());
            c2.setCellStyle(textStyle);

            // 3 单据时间（DO 已是 yyyy-MM-dd 字符串）
            org.apache.poi.ss.usermodel.Cell c3 = row.createCell(3);
            c3.setCellValue(e.getDocumentDate() == null ? "" : e.getDocumentDate());
            c3.setCellStyle(textStyle);

            // 4 单据编号
            org.apache.poi.ss.usermodel.Cell c4 = row.createCell(4);
            c4.setCellValue(e.getDocumentNumber() == null ? "" : e.getDocumentNumber());
            c4.setCellStyle(textStyle);

            // 5 支出类别
            org.apache.poi.ss.usermodel.Cell c5 = row.createCell(5);
            c5.setCellValue(e.getExpenseType() == null ? "" : e.getExpenseType());
            c5.setCellStyle(textStyle);

            // 6 结算状态（英文兜底到中文）
            String status = e.getSettlementStatus();
            if (status != null) {
                switch (status.toLowerCase()) {
                    case "settled":
                        status = "已结算";
                        break;
                    case "unsettled":
                        status = "未结算";
                        break;
                    case "partially_settled":
                        status = "部分结算";
                        break;
                    default: /* 保持原值 */
                        break;
                }
            } else status = "-";
            org.apache.poi.ss.usermodel.Cell c6 = row.createCell(6);
            c6.setCellValue(status);
            c6.setCellStyle(textStyle);

            // 金额类（7,8,9）
            java.math.BigDecimal amount = e.getAmount() == null ? java.math.BigDecimal.ZERO : e.getAmount();
            java.math.BigDecimal settled = e.getSettledAmount() == null ? java.math.BigDecimal.ZERO : e.getSettledAmount();
            java.math.BigDecimal unsettled = e.getUnsettledAmount() == null ? java.math.BigDecimal.ZERO : e.getUnsettledAmount();

            org.apache.poi.ss.usermodel.Cell c7 = row.createCell(7);
            c7.setCellValue(amount.doubleValue());
            c7.setCellStyle(numberStyle);

            org.apache.poi.ss.usermodel.Cell c8 = row.createCell(8);
            c8.setCellValue(settled.doubleValue());
            c8.setCellStyle(numberStyle);

            org.apache.poi.ss.usermodel.Cell c9 = row.createCell(9);
            c9.setCellValue(unsettled.doubleValue());
            c9.setCellStyle(numberStyle);

            sumAmount = sumAmount.add(amount);
            sumSettled = sumSettled.add(settled);
            sumUnsettled = sumUnsettled.add(unsettled);
        }

        // 6) 统计行
        org.apache.poi.ss.usermodel.Row totalRow = sheet.createRow(r++);
        totalRow.createCell(0).setCellValue("总数:" + rows.size());
        totalRow.createCell(1).setCellValue("单据总金额:" + sumAmount.stripTrailingZeros().toPlainString());
        totalRow.createCell(2).setCellValue("已结算总金额:" + sumSettled.stripTrailingZeros().toPlainString());
        totalRow.createCell(3).setCellValue("未结算总金额:" + sumUnsettled.stripTrailingZeros().toPlainString());

        // 7) 自动列宽
        for (int c = 0; c < COLS; c++) {
            sheet.autoSizeColumn(c, true);
            // 可选：再加一点 padding，避免太窄
            int width = sheet.getColumnWidth(c);
            sheet.setColumnWidth(c, Math.min(255 * 256, width + 512));
        }

        // 写出
        wb.write(out);
        wb.close();

        // 8) 下载响应
        HttpHeaders headers = new HttpHeaders();
        String filename = java.net.URLEncoder.encode("购销报表费用.xlsx", java.nio.charset.StandardCharsets.UTF_8.name());
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> resp = new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
        out.close();
        return resp;
    }


    public PageDTO<TradeExpenseReportDTO> queryTradeExpenseReportPage(TradeExpenseReportQuery query) {
        // 1.创建分页参数
        Page<TradeExpenseReportDTO> pageParam = new Page<>(query.getPageIndex(), query.getPageSize());
        // 2.查询主表分页数据
        IPage<TradeExpenseReportDTO> page = tradeExpenseMapper.queryTradeExpenseReportPage(pageParam, query);
        // 3.查询info数据
        page.getRecords().forEach(dto -> {
            dto.setInfos(tradeExpenseMapper.selectInfosById(dto.getId()));
        });
        return PageDTO.create((Page<TradeExpenseReportDTO>) page);
    }

    @SneakyThrows
    public ResponseEntity<byte[]> exportReportData(TradeExpenseReportQuery query) {
        // 1) 不分页查询所有数据（与 listBill 同条件）
        Page<TradeExpenseReportDTO> big = new Page<>(1, 100000L, false);
        Page<TradeExpenseReportDTO> page = (Page<TradeExpenseReportDTO>) tradeExpenseMapper.queryTradeExpenseReportPage(big, query);

        // 组装一对多 infos
        List<TradeExpenseReportDTO> data = page.getRecords();
        if (data != null && !data.isEmpty()) {
            for (TradeExpenseReportDTO dto : data) {
                List<ReportInfoDTO> infos = tradeExpenseMapper.selectInfosById(dto.getId());
                dto.setInfos(infos);
            }
        }

        // 2) 生成 Excel（Apache POI）
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream()) {

            org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("购销费用报表");

            // 列宽
            int[] widths = {16, 16, 24, 18, 22, 14, 14, 12};
            for (int i = 0; i < widths.length; i++) {
                sheet.setColumnWidth(i, widths[i] * 256);
            }

            // 样式
            org.apache.poi.ss.usermodel.Font titleFont = wb.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            org.apache.poi.ss.usermodel.CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
            titleStyle.setFont(titleFont);

            org.apache.poi.ss.usermodel.Font headFont = wb.createFont();
            headFont.setBold(true);
            org.apache.poi.ss.usermodel.CellStyle headStyle = wb.createCellStyle();
            headStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            headStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
            headStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headStyle.setFont(headFont);
            headStyle.setWrapText(true);

            org.apache.poi.ss.usermodel.CellStyle textStyle = wb.createCellStyle();
            textStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
            textStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            textStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            textStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            textStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            textStyle.setWrapText(true);

            // 标题行（合并 A1:H1）
            org.apache.poi.ss.usermodel.Row r0 = sheet.createRow(0);
            r0.setHeightInPoints(28);
            org.apache.poi.ss.usermodel.Cell t0 = r0.createCell(0);
            t0.setCellValue("购销费用报表");
            t0.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 7));

            // 表头
            String[] heads = {"单据类型", "所属组织", "往来单位", "单据时间", "单据编号", "支出类别", "结算状态", "金额"};
            org.apache.poi.ss.usermodel.Row r1 = sheet.createRow(1);
            for (int i = 0; i < heads.length; i++) {
                org.apache.poi.ss.usermodel.Cell c = r1.createCell(i);
                c.setCellValue(heads[i]);
                c.setCellStyle(headStyle);
            }

            // 数据：每个 cost 先 1 行，再写 infos
            int rowIdx = 2;
            int totalLines = 0;

            if (data != null) {
                for (TradeExpenseReportDTO dto : data) {
                    // cost 汇总行
                    org.apache.poi.ss.usermodel.Row rc = sheet.createRow(rowIdx++);
                    totalLines++;

                    org.apache.poi.ss.usermodel.Cell c0 = rc.createCell(0);
                    c0.setCellValue(dto.getDocumentType() == null ? "" : dto.getDocumentType());
                    c0.setCellStyle(textStyle);

                    org.apache.poi.ss.usermodel.Cell c1 = rc.createCell(1);
                    c1.setCellValue(dto.getOrganization() == null ? "" : dto.getOrganization());
                    c1.setCellStyle(textStyle);

                    org.apache.poi.ss.usermodel.Cell c2 = rc.createCell(2);
                    c2.setCellValue(dto.getPartnerUnit() == null ? "" : dto.getPartnerUnit());
                    c2.setCellStyle(textStyle);

                    org.apache.poi.ss.usermodel.Cell c3 = rc.createCell(3);
                    c3.setCellValue(dto.getDocumentDate() == null ? "" : dto.getDocumentDate());
                    c3.setCellStyle(textStyle);

                    org.apache.poi.ss.usermodel.Cell c4 = rc.createCell(4);
                    c4.setCellValue(dto.getDocumentNumber() == null ? "" : dto.getDocumentNumber());
                    c4.setCellStyle(textStyle);

                    org.apache.poi.ss.usermodel.Cell c5 = rc.createCell(5);
                    c5.setCellValue(dto.getExpenseType() == null ? "" : dto.getExpenseType());
                    c5.setCellStyle(textStyle);

                    org.apache.poi.ss.usermodel.Cell c6 = rc.createCell(6);
                    c6.setCellValue(dto.getSettlementStatus() == null ? "" : dto.getSettlementStatus());
                    c6.setCellStyle(textStyle);

                    org.apache.poi.ss.usermodel.Cell c7 = rc.createCell(7);
                    c7.setCellValue(dto.getAmount() == null ? "" : dto.getAmount().stripTrailingZeros().toPlainString());
                    c7.setCellStyle(textStyle);

                    // 子单据行
                    List<ReportInfoDTO> infos = dto.getInfos();
                    if (infos != null && !infos.isEmpty()) {
                        for (ReportInfoDTO info : infos) {
                            org.apache.poi.ss.usermodel.Row ri = sheet.createRow(rowIdx++);
                            totalLines++;

                            org.apache.poi.ss.usermodel.Cell ia = ri.createCell(0);
                            ia.setCellValue(info.getDocumentType() == null ? "" : info.getDocumentType());
                            ia.setCellStyle(textStyle);

                            org.apache.poi.ss.usermodel.Cell ib = ri.createCell(1);
                            ib.setCellValue("");
                            ib.setCellStyle(textStyle);

                            org.apache.poi.ss.usermodel.Cell ic = ri.createCell(2);
                            ic.setCellValue(info.getPartnerUnit() == null ? "" : info.getPartnerUnit());
                            ic.setCellStyle(textStyle);

                            org.apache.poi.ss.usermodel.Cell id = ri.createCell(3);
                            id.setCellValue(info.getDocumentDate() == null ? "" : info.getDocumentDate());
                            id.setCellStyle(textStyle);

                            org.apache.poi.ss.usermodel.Cell ie = ri.createCell(4);
                            ie.setCellValue(info.getDocumentNumber() == null ? "" : info.getDocumentNumber());
                            ie.setCellStyle(textStyle);

                            org.apache.poi.ss.usermodel.Cell iF = ri.createCell(5);
                            iF.setCellValue("-");
                            iF.setCellStyle(textStyle);

                            org.apache.poi.ss.usermodel.Cell ig = ri.createCell(6);
                            ig.setCellValue("-");
                            ig.setCellStyle(textStyle);

                            org.apache.poi.ss.usermodel.Cell ih = ri.createCell(7);
                            ih.setCellValue(info.getAmount() == null ? "" : info.getAmount().stripTrailingZeros().toPlainString());
                            ih.setCellStyle(textStyle);
                        }
                    }
                }
            }

            // 底部“总数:n”（合并 A~H）
            org.apache.poi.ss.usermodel.Row tail = sheet.createRow(rowIdx);
            org.apache.poi.ss.usermodel.Cell t = tail.createCell(0);
            t.setCellValue("总数:" + totalLines);
            t.setCellStyle(textStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowIdx, rowIdx, 0, 7));

            // 输出
            wb.write(bos);

            String fileName = "购销费用报表-" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

            ContentDisposition cd = ContentDisposition
                    .builder("attachment")
                    .filename(fileName, java.nio.charset.StandardCharsets.UTF_8) // 带编码的重载
                    .build();

// 如果你项目里没有 HttpHeaders#setContentDisposition(ContentDisposition) 这个方法：
            headers.set(HttpHeaders.CONTENT_DISPOSITION, cd.toString());
// 如果有该方法（Spring 5.3+）也可以：headers.setContentDisposition(cd);

            return new org.springframework.http.ResponseEntity<>(bos.toByteArray(), headers, org.springframework.http.HttpStatus.OK);
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
