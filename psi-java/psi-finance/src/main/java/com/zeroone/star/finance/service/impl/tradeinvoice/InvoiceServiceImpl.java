package com.zeroone.star.finance.service.impl.tradeinvoice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.finance.entity.BillSnapshot;
import com.zeroone.star.finance.entity.Invoice;
import com.zeroone.star.finance.mapper.tradeinvoice.InvoiceMapper;
import com.zeroone.star.finance.service.tradeinvoice.IInvoiceService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.AddTradeInvoiceDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.InvoiceReportDTO;
import com.zeroone.star.project.dto.j8.finance.tradeinvoice.TradeInvoiceDTO;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.InvoiceReportQuery;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceKey;
import com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceQuery;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 发票详情 服务实现类
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements IInvoiceService {
    @Resource
    private InvoiceMapper invoiceMapper;

    /**
     * 1.获取发票列表（条件查询+分页）
     *
     * @param query 查询参数
     * @return PageDTO<TradeInvoiceVO> 列表
     */
    public PageDTO<TradeInvoiceDTO> listTradeInvoice(TradeInvoiceQuery query) {
        Page<TradeInvoiceDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        page.setOptimizeCountSql(false); // 关闭 count 优化
        IPage<TradeInvoiceDTO> result = invoiceMapper.selectInvoiceWithConditions(page, query);
        return PageDTO.create((Page<TradeInvoiceDTO>) result);
    }

    /*
     * 2.开票（新增发票数据）
     */
    @Transactional(rollbackFor = Exception.class)
    public void addTradeInvoice(AddTradeInvoiceDTO dto) {
        // ------- 0) 基本校验（Controller 已 @Validated，这里保留关键业务校验） -------
        if (dto == null || dto.getInfos() == null || dto.getInfos().isEmpty()) {
            throw new IllegalArgumentException("至少选择一条待开票单据");
        }

        // ------- 1) 合并同一 (type,id) 的多笔金额 -------
        Map<String, BigDecimal> reqMap = new LinkedHashMap<>();
        for (AddTradeInvoiceDTO.Bill b : dto.getInfos()) {
            String type = b.getType().trim();
            String billId = b.getId().trim();
            BigDecimal amt = b.getAmount();
            if (amt == null || amt.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("开票金额必须大于0");
            }
            String key = type + "#" + billId;
            reqMap.merge(key, amt.setScale(4, RoundingMode.HALF_UP),
                    (a, c) -> a.add(c).setScale(4, RoundingMode.HALF_UP));
        }

        // ------- 2) 准备分类型的 ID 列表，便于一次性查询快照 -------
        List<String> buyIds = new ArrayList<>();
        List<String> breIds = new ArrayList<>();
        List<String> sellIds = new ArrayList<>();
        List<String> sreIds = new ArrayList<>();
        for (String key : reqMap.keySet()) {
            String[] p = key.split("#", 2);
            switch (p[0]) {
                case "buy":
                    buyIds.add(p[1]);
                    break;
                case "bre":
                    breIds.add(p[1]);
                    break;
                case "sell":
                    sellIds.add(p[1]);
                    break;
                case "sre":
                    sreIds.add(p[1]);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的单据类型: " + p[0]);
            }
        }

        // ------- 3) 查快照（actual / invStatus / openedAmount） -------
        List<BillSnapshot> snapshots = invoiceMapper.selectBillSnapshot(buyIds, breIds, sellIds, sreIds);
        Map<String, BillSnapshot> snapMap = snapshots.stream()
                .collect(Collectors.toMap(s -> s.getType() + "#" + s.getId(), s -> s));

        // ------- 4) 逐条业务校验 -------
        for (Map.Entry<String, BigDecimal> e : reqMap.entrySet()) {
            String key = e.getKey();
            BigDecimal reqAmt = e.getValue();

            BillSnapshot snap = snapMap.get(key);
            if (snap == null) {
                throw new IllegalArgumentException("单据不存在或类型不匹配: " + key);
            }
            // a) 无需开具（3）禁止再开
            if (Integer.valueOf(3).equals(snap.getInvStatus())) {
                throw new IllegalStateException("单据[" + key + "]标记为“无需开具”，禁止开票");
            }
            BigDecimal actual = snap.getActual() == null ? BigDecimal.ZERO : snap.getActual();
            BigDecimal opened = snap.getOpenedAmount() == null ? BigDecimal.ZERO : snap.getOpenedAmount();

            // b) 已全额开具禁止再开
            if (opened.compareTo(actual) >= 0 || Integer.valueOf(2).equals(snap.getInvStatus())) {
                throw new IllegalStateException("单据[" + key + "]已开完发票，禁止继续开票");
            }
            // c) 不得超开：已开 + 本次 <= actual
            if (opened.add(reqAmt).compareTo(actual) > 0) {
                BigDecimal remain = actual.subtract(opened).max(BigDecimal.ZERO);
                throw new IllegalStateException("单据[" + key + "]可开金额不足，剩余可开：" + remain);
            }
        }

        // ------- 5) 通过校验 -> 生成 Invoice DO 并批量插入 -------
        for (Map.Entry<String, BigDecimal> e : reqMap.entrySet()) {
            String[] p = e.getKey().split("#", 2);
            String type = p[0];
            String billId = p[1];

            Invoice inv = new Invoice();
            inv.setId(UUID.randomUUID().toString().replace("-", ""));
            inv.setType(type);
            inv.setClazz(billId);                 // 注意列名是 `class`
            inv.setTime(dto.getTime());
            inv.setNumber(dto.getNumber());
            inv.setTitle(dto.getTitle());
            inv.setMoney(e.getValue().setScale(4, RoundingMode.HALF_UP));
            inv.setFile(dto.getFile());
            inv.setData(dto.getData());

            int eff = invoiceMapper.insert(inv);
            if (eff != 1) {
                throw new IllegalStateException("插入发票失败: " + type + "/" + billId);
            }
        }
    }

    /**
     * 3.导出购销发票的数据为excel
     *
     * @return ResponseEntity<Object> 下载的地址
     */
    public ResponseEntity<byte[]> exportInvoiceExcel(List<TradeInvoiceKey> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            throw new IllegalArgumentException("请至少勾选一条单据");
        }

        // 1) 查库（不分页）
        List<TradeInvoiceDTO> rows = invoiceMapper.selectInvoiceListByKeys(keys);

        // 2) 合计
        BigDecimal sumTotal = BigDecimal.ZERO, sumOpened = BigDecimal.ZERO, sumUnopened = BigDecimal.ZERO;
        for (TradeInvoiceDTO r : rows) {
            sumTotal = sumTotal.add(nz(r.getTotalCount()));
            sumOpened = sumOpened.add(nz(r.getInvoicedCount()));
            sumUnopened = sumUnopened.add(nz(r.getDisinvoicedCount()));
        }

        // 3) 生成 Excel
        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Sheet sh = wb.createSheet("购销发票");
            DataFormat df = wb.createDataFormat();
            CellStyle head = wb.createCellStyle();
            Font hf = wb.createFont();
            hf.setBold(true);
            head.setFont(hf);
            CellStyle num = wb.createCellStyle();
            num.setDataFormat(df.getFormat("#,##0.00"));
            DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String[] headers = {"单据类型", "所属组织", "往来单位", "单据时间", "单据编号", "发票状态", "单据金额", "已开票金额", "未开票金额"};
            int[] colWidth = {12, 14, 22, 14, 22, 12, 16, 16, 16};

            Row h = sh.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = h.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(head);
                sh.setColumnWidth(i, colWidth[i] * 256);
            }

            int rIdx = 1;
            for (TradeInvoiceDTO r : rows) {
                Row row = sh.createRow(rIdx++);
                int c = 0;
                row.createCell(c++).setCellValue(mapType(r.getType()));
                row.createCell(c++).setCellValue(nvl(r.getFrame()));
                row.createCell(c++).setCellValue(nvl(r.getUnit()));
                row.createCell(c++).setCellValue(r.getTime() == null ? "" : r.getTime().toLocalDate().format(dateFmt));
                row.createCell(c++).setCellValue(nvl(r.getNumber()));
                row.createCell(c++).setCellValue(nvl(r.getStatus()));
                Cell t = row.createCell(c++);
                t.setCellValue(nz(r.getTotalCount()).doubleValue());
                t.setCellStyle(num);
                Cell o = row.createCell(c++);
                o.setCellValue(nz(r.getInvoicedCount()).doubleValue());
                o.setCellStyle(num);
                Cell u = row.createCell(c++);
                u.setCellValue(nz(r.getDisinvoicedCount()).doubleValue());
                u.setCellStyle(num);
            }

            Row total = sh.createRow(rIdx);
            total.createCell(0).setCellValue("合计");
            for (int i = 1; i <= 5; i++) total.createCell(i).setCellValue("");
            Cell t = total.createCell(6);
            t.setCellValue(sumTotal.doubleValue());
            t.setCellStyle(num);
            Cell o = total.createCell(7);
            o.setCellValue(sumOpened.doubleValue());
            o.setCellStyle(num);
            Cell u = total.createCell(8);
            u.setCellValue(sumUnopened.doubleValue());
            u.setCellStyle(num);

            wb.write(bos);

            // 4) 响应头（不使用 ContentDisposition，兼容性最好）
            String fileName = URLEncoder.encode("购销发票.xlsx", StandardCharsets.UTF_8.name()).replace("+", "%20");
            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + fileName);

            return new ResponseEntity<>(bos.toByteArray(), respHeaders, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }


    public PageDTO<InvoiceReportDTO> getInvoiceReportList(InvoiceReportQuery query) {
        Page<InvoiceReportDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        Page<InvoiceReportDTO> result = invoiceMapper.getInvoiceReportList(page, query);
        return PageDTO.create(result);
    }

    public ResponseEntity<byte[]> exportInvoiceReportExcel(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("请至少勾选一条发票记录");
        }

        List<InvoiceReportDTO> rows = invoiceMapper.selectInvoiceReportByIds(ids);

        // 合计
        BigDecimal sumTotal = BigDecimal.ZERO;
        BigDecimal sumInv = BigDecimal.ZERO;
        for (InvoiceReportDTO r : rows) {
            sumTotal = sumTotal.add(nz(r.getTotal()));
            sumInv = sumInv.add(nz(r.getInvMoney()));
        }

        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Sheet sh = wb.createSheet("购销发票报表");
            // 列宽
            int[] widths = {12, 14, 22, 14, 22, 12, 14, 16, 20, 12, 16};
            for (int i = 0; i < widths.length; i++) sh.setColumnWidth(i, widths[i] * 256);

            // 样式
            DataFormat df = wb.createDataFormat();

            CellStyle titleStyle = wb.createCellStyle();
            Font tf = wb.createFont();
            tf.setBold(true);
            tf.setFontHeightInPoints((short) 14);
            titleStyle.setFont(tf);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle head = wb.createCellStyle();
            Font hf = wb.createFont();
            hf.setBold(true);
            head.setFont(hf);
            head.setAlignment(HorizontalAlignment.CENTER);
            head.setVerticalAlignment(VerticalAlignment.CENTER);
            head.setBorderTop(BorderStyle.THIN);
            head.setBorderBottom(BorderStyle.THIN);
            head.setBorderLeft(BorderStyle.THIN);
            head.setBorderRight(BorderStyle.THIN);
            head.setWrapText(true);

            CellStyle txt = wb.createCellStyle();
            txt.setBorderTop(BorderStyle.THIN);
            txt.setBorderBottom(BorderStyle.THIN);
            txt.setBorderLeft(BorderStyle.THIN);
            txt.setBorderRight(BorderStyle.THIN);

            CellStyle dateStyle = wb.createCellStyle();
            dateStyle.cloneStyleFrom(txt);
            dateStyle.setDataFormat(df.getFormat("yyyy-mm-dd"));

            CellStyle num = wb.createCellStyle();
            num.cloneStyleFrom(txt);
            num.setDataFormat(df.getFormat("#,##0.00"));

            // 标题行（合并 A1~K1）
            Row titleRow = sh.createRow(0);
            titleRow.setHeightInPoints(24);
            Cell t0 = titleRow.createCell(0);
            t0.setCellValue("购销发票报表");
            t0.setCellStyle(titleStyle);
            sh.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 10));

            // 表头
            String[] headers = {"单据类型", "所属组织", "往来单位", "单据时间", "单据编号", "单据金额", "开票时间", "发票号码", "发票抬头", "发票金额", "备注信息"};
            Row h = sh.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell c = h.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(head);
            }

            DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // 数据
            int rIdx = 3;
            for (InvoiceReportDTO r : rows) {
                Row row = sh.createRow(rIdx++);
                int c = 0;

                Cell c0 = row.createCell(c++);
                c0.setCellValue(mapType(r.getType()));
                c0.setCellStyle(txt);
                Cell c1 = row.createCell(c++);
                c1.setCellValue(nvl(r.getFrame()));
                c1.setCellStyle(txt);
                Cell c2 = row.createCell(c++);
                c2.setCellValue(nvl(r.getBusinessUnit()));
                c2.setCellStyle(txt);

                Cell c3 = row.createCell(c++);
                c3.setCellStyle(dateStyle);
                if (r.getTime() != null) c3.setCellValue(r.getTime().format(d));

                Cell c4 = row.createCell(c++);
                c4.setCellValue(nvl(r.getNumber()));
                c4.setCellStyle(txt);

                Cell c5 = row.createCell(c++);
                c5.setCellStyle(num);
                c5.setCellValue(nz(r.getTotal()).doubleValue());

                Cell c6 = row.createCell(c++);
                c6.setCellStyle(dateStyle);
                if (r.getInvTime() != null) c6.setCellValue(r.getInvTime().format(d));

                Cell c7 = row.createCell(c++);
                c7.setCellValue(nvl(r.getInvNumber()));
                c7.setCellStyle(txt);
                Cell c8 = row.createCell(c++);
                c8.setCellValue(nvl(r.getTitle()));
                c8.setCellStyle(txt);

                Cell c9 = row.createCell(c++);
                c9.setCellStyle(num);
                c9.setCellValue(nz(r.getInvMoney()).doubleValue());

                Cell c10 = row.createCell(c++);
                c10.setCellValue(nvl(r.getData()));
                c10.setCellStyle(txt);
            }

            // 合计行（A:总数，F:单据总金额，J:发票总金额）
            Row total = sh.createRow(rIdx);
            for (int i = 0; i < headers.length; i++) total.createCell(i).setCellStyle(txt);
            total.getCell(0).setCellValue("总数:" + rows.size());
            total.getCell(5).setCellValue("单据总金额:" + sumTotal);
            total.getCell(9).setCellValue("发票总金额:" + sumInv);

//            String fileName = URLEncoder.encode("购销发票.xlsx", StandardCharsets.UTF_8.name()).replace("+", "%20");
//            HttpHeaders respHeaders = new HttpHeaders();
//            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            respHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + fileName);
//
//            return new ResponseEntity<>(bos.toByteArray(), respHeaders, HttpStatus.OK);

            // 输出
            wb.write(bos);
            String fileName = URLEncoder.encode("购销发票报表.xlsx", StandardCharsets.UTF_8.name()).replace("+", "%20");
            HttpHeaders resp = new HttpHeaders();
            resp.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            resp.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + fileName);
            return new ResponseEntity<>(bos.toByteArray(), resp, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage(), e);
        }
    }

    // -------- 工具函数（可复用到其他导出） --------
    private static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }

    private static String mapType(String t) {
        if ("buy".equalsIgnoreCase(t)) return "采购单";
        if ("bre".equalsIgnoreCase(t)) return "采购退货单";
        if ("sell".equalsIgnoreCase(t)) return "销售单";
        if ("sre".equalsIgnoreCase(t)) return "销售退货单";
        return t;
    }
}
