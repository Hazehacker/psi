package com.zeroone.star.finance.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zeroone.star.finance.entity.Ice;
import com.zeroone.star.finance.entity.Otherincomeexportdetailreportview;
import com.zeroone.star.finance.entity.Otherincomeexportsimplereportview;
import com.zeroone.star.finance.mapper.otherincome.OtherIncomeMapper;
import com.zeroone.star.finance.service.IOtherIncomeService;
import com.zeroone.star.finance.service.IOtherincomeexportdetailreportviewService;
import com.zeroone.star.finance.service.IOtherincomeexportsimplereportviewService;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.j8.finance.otherincome.AddOtherIncomeDTO;
import com.zeroone.star.project.dto.j8.finance.otherincome.AddOtherIncomeInfoDTO;
import com.zeroone.star.project.dto.j8.finance.otherincome.OtherIncomeExportDetailedReportDTO;
import com.zeroone.star.project.dto.j8.finance.otherincome.OtherIncomeExportSimpleReportDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// com.zeroone.star.finance.service.impl.IOtherIncomeServiceImpl
@Service
@Slf4j
public class IOtherIncomeServiceImpl implements IOtherIncomeService {

    @Resource
    private IOtherincomeexportsimplereportviewService simpleViewService;

    @Resource
    private IOtherincomeexportdetailreportviewService detailViewService;

    @Resource
    private EasyExcelComponent excel;

    @Resource
    private OtherIncomeMapper otherIncomeMapper;

    @Transactional(rollbackFor = Exception.class)
    public String reviseOtherIncome(AddOtherIncomeDTO dto) {
        // ---------- 0) 基本校验 ----------
        if (dto.getId() == null || dto.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("id 不能为空");
        }
        if (dto.getInfo() == null || dto.getInfo().isEmpty()) {
            throw new IllegalArgumentException("明细不能为空");
        }

        // ---------- 1) 单据存在 & 未审核 ----------
        Ice head = otherIncomeMapper.selectIceStatus(dto.getId());
        if (head == null) {
            throw new IllegalArgumentException("单据不存在");
        }
        if (head.getExamine() != null && head.getExamine() == 1) {
            throw new IllegalStateException("已审核单据不能修改");
        }

        // ---------- 2) 外键存在性 ----------
        if (dto.getCustomer() != null && otherIncomeMapper.existsCustomer(dto.getCustomer()) == 0) {
            throw new IllegalArgumentException("客户不存在: " + dto.getCustomer());
        }
        if (dto.getAccount() == null || otherIncomeMapper.existsAccount(dto.getAccount()) == 0) {
            throw new IllegalArgumentException("结算账户不存在: " + dto.getAccount());
        }
        if (dto.getPeople() != null && !dto.getPeople().isEmpty()
                && otherIncomeMapper.existsPeople(dto.getPeople()) == 0) {
            throw new IllegalArgumentException("关联人员不存在: " + dto.getPeople());
        }
        // IET 列表
        List<String> ietIds = dto.getInfo().stream().map(AddOtherIncomeInfoDTO::getIet).distinct().collect(Collectors.toList());
        List<String> ietsExists = ietIds.isEmpty() ? Collections.emptyList() : otherIncomeMapper.selectExistingIetIds(ietIds);
        if (ietsExists.size() != ietIds.size()) {
            // 找出缺失的
            HashSet<String> ok = new HashSet<>(ietsExists);
            List<String> missing = ietIds.stream().filter(x -> !ok.contains(x)).collect(Collectors.toList());
            throw new IllegalArgumentException("收入类别(IET)不存在: " + String.join(",", missing));
        }

        // ---------- 3) 金额关系 ----------
        BigDecimal total = nz(dto.getTotal());
        BigDecimal actual = nz(dto.getActual());
        BigDecimal money = nz(dto.getMoney());

        if (!(gt(total, actual) && gt(actual, money))) {
            throw new IllegalArgumentException("金额关系不合法：应满足 单据金额 > 实际金额 > 实收金额");
        }

        BigDecimal sumInfo = dto.getInfo().stream()
                .map(x -> x.getMoney() == null ? BigDecimal.ZERO : x.getMoney())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (neq(scale4(sumInfo), scale4(money))) {
            throw new IllegalArgumentException("实收金额必须等于明细结算金额合计");
        }

        // ---------- 4) 更新单头（WHERE examine=0 兜底） ----------
        int u = otherIncomeMapper.updateIceHeadIfUnexamined(dto);
        if (u == 0) {
            throw new IllegalStateException("单据不存在或已审核，无法修改");
        }

        // ---------- 5) 明细删旧插新 ----------
        otherIncomeMapper.deleteInfosByPid(dto);
        otherIncomeMapper.batchInsertInfos(dto);

        // （可选）库端合计复核
        // BigDecimal dbSum = otherIncomeMapper.sumInfoMoney(dto);
        // if (neq(scale4(dbSum), scale4(money))) {
        //     throw new IllegalStateException("数据库端合计与实收金额不一致");
        // }

        return dto.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteOtherIncome(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("ids 不能为空");
        }
        // 1) 禁删已审核
        List<String> audited = otherIncomeMapper.listAuditedIds(ids);
        if (audited != null && !audited.isEmpty()) {
            throw new IllegalStateException("存在已审核单据，禁止删除：" + String.join(",", audited));
        }
        // 2) 先删明细
        int di = otherIncomeMapper.deleteInfosByPids(ids);
        // 3) 再删主表
        int dh = otherIncomeMapper.deleteIceByIds(ids);
        log.info("batchDeleteOtherIncome -> delete infos={}, heads={}", di, dh);
        return dh; // 返回删除的主表条数
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importOtherIncome(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }

        // 1) 读取三张表（oce / ice 兼容；每张表单独打开字节流）
        List<IceHeadRow> heads = tryRead(file, "oce", IceHeadRow.class);
        if (heads.isEmpty()) {
            heads = tryRead(file, "ice", IceHeadRow.class);
        }
        List<IceInfoRow> infos = tryRead(file, "info", IceInfoRow.class);
        List<IceBillRow> bills = tryRead(file, "bill", IceBillRow.class);

        log.info("import read: heads={}, infos={}, bills={}", heads.size(), infos.size(), bills.size());
        if (heads.isEmpty()) throw new IllegalArgumentException("单头页为空（oce/ice 都未读到数据）");
        if (infos == null) infos = Collections.emptyList();
        if (bills == null) bills = Collections.emptyList();

        // 2) 去空行
        heads.removeIf(IceHeadRow::isBlank);
        infos.removeIf(IceInfoRow::isBlank);
        bills.removeIf(IceBillRow::isBlank);
        if (heads.isEmpty()) throw new IllegalArgumentException("单头页有效数据为空");

        // 3) 名称 -> id 字典
        Map<String, ?> frameMap = otherIncomeMapper.mapFrameName2Id();
        Map<String, ?> custMap = otherIncomeMapper.mapCustomerName2Id();
        Map<String, ?> acctMap = otherIncomeMapper.mapAccountName2Id();
        Map<String, ?> peopleMap = otherIncomeMapper.mapPeopleName2Id();
        Map<String, ?> ietMap = otherIncomeMapper.mapIetName2Id();


        // 4) 单号唯一（Excel 内 & DB）
        Set<String> numbers = heads.stream().map(IceHeadRow::getNumber).collect(Collectors.toSet());
        if (numbers.size() != heads.size()) {
            Map<String, Long> cnt = heads.stream()
                    .collect(Collectors.groupingBy(IceHeadRow::getNumber, LinkedHashMap::new, Collectors.counting()));
            List<String> dups = cnt.entrySet().stream()
                    .filter(e -> e.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
            throw new IllegalArgumentException("Excel 内存在重复单号: " + String.join(",", dups));
        }
        List<String> existNums = otherIncomeMapper.listExistingIceNumbers(new ArrayList<>(numbers));
        if (!existNums.isEmpty()) {
            throw new IllegalArgumentException("数据库已存在单号: " + String.join(",", existNums));
        }

        // 5) 关联性校验
        List<String> orphanInfo = infos.stream().map(IceInfoRow::getNumber)
                .filter(n -> !numbers.contains(n)).distinct().collect(Collectors.toList());
        if (!orphanInfo.isEmpty()) throw new IllegalArgumentException("明细(info)无法映射到单头: " + orphanInfo);

        List<String> orphanBill = bills.stream().map(IceBillRow::getNumber)
                .filter(n -> !numbers.contains(n)).distinct().collect(Collectors.toList());
        if (!orphanBill.isEmpty()) throw new IllegalArgumentException("核销(bill)无法映射到单头: " + orphanBill);

        // 6) 名称存在性校验
        List<String> missing = new ArrayList<>();
        for (IceHeadRow h : heads) {
            if (blank(h.getFrameName()) || !frameMap.containsKey(h.getFrameName()))
                missing.add("组织不存在: " + nvl(h.getFrameName()));
            if (!blank(h.getCustomerName()) && !custMap.containsKey(h.getCustomerName()))
                missing.add("客户不存在: " + nvl(h.getCustomerName()));
            if (blank(h.getAccountName()) || !acctMap.containsKey(h.getAccountName()))
                missing.add("账户不存在: " + nvl(h.getAccountName()));
            if (!blank(h.getPeopleName()) && !peopleMap.containsKey(h.getPeopleName()))
                missing.add("人员不存在: " + nvl(h.getPeopleName()));
        }
        for (String ietName : infos.stream().map(IceInfoRow::getIetName).filter(s -> !blank(s)).collect(Collectors.toSet())) {
            if (!ietMap.containsKey(ietName)) missing.add("收入类别不存在: " + ietName);
        }
        if (!missing.isEmpty()) throw new IllegalArgumentException(String.join("; ", missing));

        // 7) 组装 & 金额关系校验
        Map<String, String> idByNumber = heads.stream().collect(Collectors.toMap(
                IceHeadRow::getNumber, h -> UUID.randomUUID().toString().replace("-", "")
        ));

        Map<String, BigDecimal> sumInfoByNumber = infos.stream().collect(
                Collectors.groupingBy(IceInfoRow::getNumber,
                        Collectors.mapping(IceInfoRow::getMoney,
                                Collectors.reducing(BigDecimal.ZERO, IOtherIncomeServiceImpl::nz, BigDecimal::add)))
        );

        List<Ice> insertHeads = new ArrayList<>();
        for (IceHeadRow h : heads) {
            String id = idByNumber.get(h.getNumber());
            BigDecimal total = scale4(h.getTotal());
            BigDecimal actual = scale4(h.getActual());
            BigDecimal money = scale4(h.getMoney());
            BigDecimal sumInfo = scale4(sumInfoByNumber.getOrDefault(h.getNumber(), BigDecimal.ZERO));

            if (!(gt(total, actual) && gt(actual, money))) {
                throw new IllegalArgumentException("金额关系不合法（" + h.getNumber() + "）：应满足 单据金额>实际金额>单据收款");
            }
            if (money.compareTo(sumInfo) != 0) {
                throw new IllegalArgumentException("明细合计不等于单据收款（" + h.getNumber() + "）：收款=" + money + "，明细合计=" + sumInfo);
            }

            Ice e = new Ice();
            e.setId(id);
            e.setFrame(asId(frameMap.get(h.getFrameName())));
            e.setCustomer(blank(h.getCustomerName()) ? null : asId(custMap.get(h.getCustomerName())));
            e.setTime(h.getTime());
            e.setNumber(h.getNumber());
            e.setTotal(total);
            e.setActual(actual);
            e.setMoney(money);
            e.setAccount(asId(acctMap.get(h.getAccountName())));
            e.setPeople(blank(h.getPeopleName()) ? null : asId(peopleMap.get(h.getPeopleName())));
            e.setFile(h.getFile());
            e.setData(limit256(h.getData()));
            e.setMore(h.getMore());
            e.setExamine(0);
            e.setNucleus(0);
            e.setUser(h.getUser());
            insertHeads.add(e);
        }

        List<Map<String, Object>> insertInfos = new ArrayList<>();
        for (IceInfoRow r : infos) {
            Map<String, Object> it = new HashMap<>();
            it.put("id", UUID.randomUUID().toString().replace("-", ""));
            it.put("pid", idByNumber.get(r.getNumber()));
            it.put("iet", asId(ietMap.get(r.getIetName())));
            it.put("money", scale4(nz(r.getMoney())));
            it.put("data", limit256(r.getData()));
            insertInfos.add(it);
        }

        List<Map<String, Object>> insertBills = new ArrayList<>();
        for (IceBillRow b : bills) {
            Map<String, Object> it = new HashMap<>();
            it.put("id", UUID.randomUUID().toString().replace("-", ""));
            it.put("pid", idByNumber.get(b.getNumber()));
            it.put("type", nvl(b.getType()));
            it.put("source", nvl(b.getSource()));
            it.put("time", b.getTime());
            it.put("money", scale4(nz(b.getMoney())));
            insertBills.add(it);
        }

        // 8) 批量写入
        int h = otherIncomeMapper.batchInsertIce(insertHeads);
        if (!insertInfos.isEmpty()) otherIncomeMapper.batchInsertInfosMap(insertInfos);
        if (!insertBills.isEmpty()) otherIncomeMapper.batchInsertBillsMap(insertBills);

        log.info("import result: headsInserted={}, infosInserted={}, billsInserted={}", h, insertInfos.size(), insertBills.size());
        if (h == 0 && insertInfos.isEmpty() && insertBills.isEmpty()) {
            throw new IllegalStateException("未导入任何数据：请检查 sheet 名与列头是否匹配");
        }
        return "导入成功：单头 " + h + " 条，明细 " + insertInfos.size() + " 条，核销 " + insertBills.size() + " 条";
    }

    /* -------------------- 8. 简表导出（合并到本 Service） -------------------- */
    @Override
    public ResponseEntity<byte[]> exportSimpleReportExcel(List<String> iceIds) throws IOException {
        // 1) 查视图
        List<Otherincomeexportsimplereportview> viewList = simpleViewService.list(iceIds);
        if (viewList == null || viewList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 2) 以 iceId 分组，并且“核销金额”要按 billId 去重后求和（避免 info×bill 笛卡尔重复）
        Map<String, List<Otherincomeexportsimplereportview>> byIce =
                viewList.stream().collect(Collectors.groupingBy(Otherincomeexportsimplereportview::getId));

        List<OtherIncomeExportSimpleReportDTO> dtos = new ArrayList<>();

        for (Map.Entry<String, List<Otherincomeexportsimplereportview>> e : byIce.entrySet()) {
            String iceId = e.getKey();
            List<Otherincomeexportsimplereportview> group = e.getValue();
            Otherincomeexportsimplereportview first = group.get(0);

            // bill_money 去重（按 billId）再求和
            BigDecimal billSum = group.stream()
                    .filter(x -> x.getBillId() != null)
                    .collect(Collectors.toMap(
                            Otherincomeexportsimplereportview::getBillId,
                            x -> x,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ))
                    .values().stream()
                    .map(x -> x.getBillMoney() != null ? x.getBillMoney() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 组装一条汇总化后的行（简表一条=一张单）
            OtherIncomeExportSimpleReportDTO dto = new OtherIncomeExportSimpleReportDTO();
            dto.setFrame(first.getFrame());
            dto.setCustomer(first.getCustomer());
            dto.setTime(first.getTime());
            dto.setNumber(first.getNumber());
            dto.setTotal(first.getTotal());
            dto.setActual(first.getActual());
            dto.setMoney(first.getMoney());
            dto.setBillMoney(billSum);
            dto.setAccount(first.getAccount());
            dto.setPeople(first.getPeople());
            dto.setExamine(first.getExamine());
            Integer nucleus = first.getNucleus();
            if (nucleus != null) {
                dto.setNucleus(nucleus == 0 ? "未核销" : nucleus == 1 ? "部分核销" : "已核销");
            }
            dto.setUser(first.getUser());
            dto.setData(first.getData());
            dtos.add(dto);
        }

        // 3) 末尾加一行“合计”
        int totalCount = dtos.size();
        BigDecimal sumTotal = dtos.stream().map(x -> nv(x.getTotal())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumActual = dtos.stream().map(x -> nv(x.getActual())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumMoney = dtos.stream().map(x -> nv(x.getMoney())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumBill = dtos.stream().map(x -> nv(x.getBillMoney())).reduce(BigDecimal.ZERO, BigDecimal::add);

        OtherIncomeExportSimpleReportDTO summary = new OtherIncomeExportSimpleReportDTO();
        summary.setFrame("合计");
        summary.setCustomer("总数：" + totalCount);
        summary.setTotal(sumTotal);
        summary.setActual(sumActual);
        summary.setMoney(sumMoney);
        summary.setBillMoney(sumBill);
        dtos.add(summary);

        // 4) 写 Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        excel.export("其他收入单简单报表", out, OtherIncomeExportSimpleReportDTO.class, dtos);

        // 5) 返回
        HttpHeaders headers = buildAttachmentHeader(
                "其他收入单简单报表-批量(" + totalCount + "条)-" + now() + ".xlsx");
        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
    }

    /* -------------------- 9. 详表导出（修正为“去重+行展开”） -------------------- */
    @Override
    public ResponseEntity<byte[]> exportDetailedReportExcel(List<String> iceIds, HttpServletRequest request) throws IOException {
        List<Otherincomeexportdetailreportview> viewList = detailViewService.list(iceIds);
        if (viewList == null || viewList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 分组：每个 iceId 一个 Excel
        Map<String, List<Otherincomeexportdetailreportview>> byIce =
                viewList.stream().collect(Collectors.groupingBy(Otherincomeexportdetailreportview::getIceId));

        ByteArrayOutputStream zipOut = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(zipOut);

        for (Map.Entry<String, List<Otherincomeexportdetailreportview>> e : byIce.entrySet()) {
            String iceId = e.getKey();
            List<Otherincomeexportdetailreportview> group = e.getValue();
            Otherincomeexportdetailreportview head = group.get(0);

            // ====== 1) 明细行去重：按 iceInfoId 去重，避免 info × bill 产生的重复行 ======
            List<Otherincomeexportdetailreportview> detailRows = new ArrayList<>();
            LinkedHashMap<String, Otherincomeexportdetailreportview> infoSeen = new LinkedHashMap<>();
            for (Otherincomeexportdetailreportview r : group) {
                String infoId = r.getIceInfoId();   // 确保实体里有 getIceInfoId()
                if (infoId != null && !infoSeen.containsKey(infoId)) {
                    infoSeen.put(infoId, r);
                }
            }
            detailRows.addAll(infoSeen.values());

            // ====== 2) 核销金额汇总：按 iceBillId 去重后 SUM(billMoney) ======
            BigDecimal billSum = BigDecimal.ZERO;
            LinkedHashMap<String, BigDecimal> billSeen = new LinkedHashMap<>();
            for (Otherincomeexportdetailreportview r : group) {
                String billId = r.getIceBillId();   // 确保实体里有 getIceBillId()
                if (billId != null && !billSeen.containsKey(billId)) {
                    billSeen.put(billId, r.getBillMoney() == null ? BigDecimal.ZERO : r.getBillMoney()); // 确保实体里有 getBillMoney()
                }
            }
            for (BigDecimal v : billSeen.values()) {
                billSum = billSum.add(v);
            }

            /* 关键修正 3：生成 DTO —— 每条 detail = 一行
               - 列：收入类别(iet) / 结算金额(money) / 备注(data)
               - 顶部标题【客户/日期/编号】与底部汇总【total/account/bill/people/data】：
                 只在第一行写，后续行置 null，便于你现有 Excel 模板合并单元格/放到上/下方。 */
            List<OtherIncomeExportDetailedReportDTO> dtos = new ArrayList<>();
            for (int i = 0; i < detailRows.size(); i++) {
                Otherincomeexportdetailreportview r = detailRows.get(i);
                OtherIncomeExportDetailedReportDTO dto = new OtherIncomeExportDetailedReportDTO();

                // 顶部标题（只在第一行写）
                if (i == 0) {
                    dto.setCustomer(head.getCustomer());
                    dto.setTime(head.getTime());
                    dto.setNumber(head.getNumber());
                }

                // 明细区域
                dto.setIet(r.getIet());             // ★ 你之前是 null，这里一定要从视图取
                dto.setMoney(r.getMoney());         // = ice_info.money（结算金额）
                dto.setData(r.getData());           // = ice_info.data（备注）

                // 底部汇总（只在第一行写）
                if (i == 0) {
                    dto.setTotal(head.getTotal());
                    dto.setAccount(head.getAccount());
                    dto.setBill_money(billSum);
                    dto.setPeople(head.getPeople());
                    // 如果需要把单头备注也放底部，可用 head.getHeadData()，或仍用 r.getData()
                }
                dtos.add(dto);
            }

            // 写单个 Excel → Zip
            byte[] xls = buildDetailExcelByPOI(head, detailRows, billSum); // ★ 用下面的方法
            String excelFileName = String.format("其他收入单详细报表-%s-%s.xlsx",
                    head.getNumber(), now()); // 用单据编号更友好
            zos.putNextEntry(new ZipEntry(excelFileName));
            zos.write(xls);
            zos.closeEntry();
        }

        zos.close();

        HttpHeaders headers = buildAttachmentHeader(
                "其他收入单详细报表-批量(" + byIce.size() + "组)-" + now() + ".zip");
        return new ResponseEntity<>(zipOut.toByteArray(), headers, HttpStatus.OK);
    }

    /* -------------------- 私有小工具 -------------------- */
    private static BigDecimal nv(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private static String now() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private static HttpHeaders buildAttachmentHeader(String filename) {
        HttpHeaders headers = new HttpHeaders();
        try {
            String encoded = java.net.URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            headers.set("Content-Disposition", String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s", encoded, encoded));
        } catch (Exception ignore) {
            headers.setContentDispositionFormData("attachment", filename);
        }
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return headers;
    }

    private byte[] buildDetailExcelByPOI(
            Otherincomeexportdetailreportview head,
            List<Otherincomeexportdetailreportview> detailRows,
            BigDecimal billSum) throws IOException {

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sh = wb.createSheet("其他收入单详细报表");

            // 列宽（按效果微调）
            sh.setColumnWidth(0, 18 * 256); // A
            sh.setColumnWidth(1, 18 * 256); // B
            sh.setColumnWidth(2, 18 * 256); // C
            sh.setColumnWidth(3, 18 * 256); // D
            sh.setColumnWidth(4, 26 * 256); // E

            // 基础样式
            Font bold = wb.createFont();
            bold.setBold(true);
            Font titleFont = wb.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);

            CellStyle title = wb.createCellStyle();
            title.setFont(titleFont);
            title.setAlignment(HorizontalAlignment.CENTER);
            title.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle th = wb.createCellStyle();
            th.setFont(bold);
            th.setAlignment(HorizontalAlignment.CENTER);
            th.setVerticalAlignment(VerticalAlignment.CENTER);
            th.setBorderTop(BorderStyle.THIN);
            th.setBorderBottom(BorderStyle.THIN);
            th.setBorderLeft(BorderStyle.THIN);
            th.setBorderRight(BorderStyle.THIN);

            CellStyle td = wb.createCellStyle();
            td.setBorderTop(BorderStyle.THIN);
            td.setBorderBottom(BorderStyle.THIN);
            td.setBorderLeft(BorderStyle.THIN);
            td.setBorderRight(BorderStyle.THIN);

            // Row0：标题  合并 A1:E1
            Row r0 = sh.createRow(0);
            r0.setHeightInPoints(24);
            Cell c00 = r0.createCell(0);
            c00.setCellValue("其它收入单");
            c00.setCellStyle(title);
            sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            // Row1：客户 / 单据日期 / 单据编号
            Row r1 = sh.createRow(1);
            Cell r1a = r1.createCell(0);
            r1a.setCellValue("客户:" + nvl(head.getCustomer()));
            Cell r1c = r1.createCell(2);
            r1c.setCellValue("单据日期:" + fmtDate(head.getTime()));
            Cell r1e = r1.createCell(4);
            r1e.setCellValue("单据编号:" + nvl(head.getNumber()));
            // 合并 [A2:B2], [C2:D2]
            sh.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
            sh.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));

            // Row2：明细表头  收入类别 | 结算金额 | 备注信息(横跨 C~E)
            Row r2 = sh.createRow(2);
            Cell h0 = r2.createCell(0);
            h0.setCellValue("收入类别");
            h0.setCellStyle(th);
            Cell h1 = r2.createCell(1);
            h1.setCellValue("结算金额");
            h1.setCellStyle(th);
            Cell h2 = r2.createCell(2);
            h2.setCellValue("备注信息");
            h2.setCellStyle(th);
            // 备注信息合并 C3:E3
            sh.addMergedRegion(new CellRangeAddress(2, 2, 2, 4));
            // 为合并区的其它单元格写样式边框
            Cell h3 = r2.createCell(3);
            h3.setCellStyle(th);
            Cell h4 = r2.createCell(4);
            h4.setCellStyle(th);

            // 明细行
            int rowIdx = 3;
            for (Otherincomeexportdetailreportview d : detailRows) {
                Row r = sh.createRow(rowIdx++);
                Cell c0 = r.createCell(0);
                c0.setCellValue(nvl(d.getIet()));
                c0.setCellStyle(td);
                Cell c1 = r.createCell(1);
                c1.setCellValue(nv(d.getMoney()).doubleValue());
                c1.setCellStyle(td);
                Cell c2 = r.createCell(2);
                c2.setCellValue(nvl(d.getData()));
                c2.setCellStyle(td);
                // 备注合并 C~E
                Cell c3 = r.createCell(3);
                c3.setCellStyle(td);
                Cell c4 = r.createCell(4);
                c4.setCellStyle(td);
                sh.addMergedRegion(new CellRangeAddress(r.getRowNum(), r.getRowNum(), 2, 4));
            }

            // 底部汇总行： 单据金额 | 结算账户 | 核销金额 | 关联人员 | 备注信息
            Row rf = sh.createRow(rowIdx++);
            Cell f0 = rf.createCell(0);
            f0.setCellValue("单据金额:" + nv(head.getTotal()));
            f0.setCellStyle(th);
            Cell f1 = rf.createCell(1);
            f1.setCellValue("结算账户:" + nvl(head.getAccount()));
            f1.setCellStyle(th);
            Cell f2 = rf.createCell(2);
            f2.setCellValue("核销金额:" + nv(billSum));
            f2.setCellStyle(th);
            Cell f3 = rf.createCell(3);
            f3.setCellValue("关联人员:" + nvl(head.getPeople()));
            f3.setCellStyle(th);
            Cell f4 = rf.createCell(4);
            f4.setCellValue("备注信息:" + nvl(head.getData()));
            f4.setCellStyle(th);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            return bos.toByteArray();
        }
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }

    private static String fmtDate(Object t) {
        if (t == null) return "";
        if (t instanceof java.time.LocalDateTime) {
            return ((java.time.LocalDateTime) t).toLocalDate().toString();
        }
        if (t instanceof java.util.Date) {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format((java.util.Date) t);
        }
        return String.valueOf(t);
    }

    // --------- BigDecimal 工具 ----------
    private static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private static BigDecimal scale4(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v.setScale(4, RoundingMode.HALF_UP);
    }

    private static boolean gt(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) > 0;
    }

    private static boolean neq(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) != 0;
    }

    @Data
    public static class IceHeadRow {
        @ExcelProperty("所属组织")
        private String frameName;
        @ExcelProperty("客户名称")
        private String customerName;
        @ExcelProperty("单据时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime time;
        @ExcelProperty("单据编号")
        private String number;
        @ExcelProperty("单据金额")
        private BigDecimal total;
        @ExcelProperty("实际金额")
        private BigDecimal actual;
        @ExcelProperty("单据收款")
        private BigDecimal money;
        @ExcelProperty("结算账户")
        private String accountName;
        @ExcelProperty("关联人员")
        private String peopleName;
        @ExcelProperty("附件")
        private String file;
        @ExcelProperty("备注信息")
        private String data;
        @ExcelProperty("扩展信息")
        private String more;
        @ExcelProperty("制单人")
        private String user;

        boolean isBlank() {
            return blank(number);
        }
        // getters/setters 由 @Data 生成
    }

    @Data
    public static class IceInfoRow {
        @ExcelProperty("单据编号")
        private String number;
        @ExcelProperty("收入类别")
        private String ietName;    // 映射到 iet.name
        @ExcelProperty("结算金额")
        private BigDecimal money;
        @ExcelProperty("备注信息")
        private String data;

        boolean isBlank() {
            return blank(number) && blank(ietName);
        }
    }

    @Data
    public static class IceBillRow {
        @ExcelProperty("单据编号")
        private String number;
        @ExcelProperty("核销类型")
        private String type;       // 文本，如“收款核销”
        @ExcelProperty("来源单号")
        private String source;
        @ExcelProperty("核销时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime time;
        @ExcelProperty("核销金额")
        private BigDecimal money;

        boolean isBlank() {
            return blank(number) && money == null;
        }
    }

    // ====== 小工具 ======
    private static boolean blank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String limit256(String s) {
        if (s == null) return null;
        return s.length() <= 256 ? s : s.substring(0, 256);
    }


    @SuppressWarnings("unchecked")
    private static String asId(Object v) {
        if (v == null) return null;
        if (v instanceof String) return ((String) v).trim();
        if (v instanceof Map) {
            Object id = ((Map<?, ?>) v).get("id");
            return id == null ? null : String.valueOf(id).trim();
        }
        return String.valueOf(v).trim();
    }

    private <T> List<T> tryRead(MultipartFile file, String sheet, Class<T> clazz) {
        try {
            byte[] bytes = file.getBytes();
            try (InputStream in = new ByteArrayInputStream(bytes)) {
                return EasyExcel.read(in, clazz, null).sheet(sheet).doReadSync();
            }
        } catch (Exception e) {
            log.warn("read sheet failed: {}", sheet);
            return Collections.emptyList();
        }
    }
}
