package com.zeroone.star.purchase.service.impl;
import com.alibaba.excel.EasyExcel;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.query.j3.capital.PurchaseNoteQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.purchase.excel.*;
import com.zeroone.star.purchase.DO.BuyDO;
import com.zeroone.star.purchase.DO.BuyInfoDO;
import com.zeroone.star.purchase.mapper.BuyInfoMapper;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteListDTO;
import com.zeroone.star.purchase.DO.BuyDO;
import com.zeroone.star.purchase.DO.GoodsDO;
import com.zeroone.star.purchase.VO.BuyVO;
import com.zeroone.star.purchase.mapper.BuyMapper;
import com.zeroone.star.purchase.service.IBuyInfoService;
import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteGenerateDataDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteOtherGenerateDataDTO;
import com.zeroone.star.purchase.DO.*;
import com.zeroone.star.purchase.mapper.BorInfoMapper;
import com.zeroone.star.purchase.mapper.BuyInfoMapper;
import com.zeroone.star.purchase.mapper.BuyMapper;
import com.zeroone.star.purchase.mapper.GoodsMapper;
import com.zeroone.star.purchase.service.IBuyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.purchase.utils.ExcelHelper;
import com.zeroone.star.purchase.utils.ExcelStyleUtil;
import com.zeroone.star.purchase.utils.ParseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.zeroone.star.project.dto.j3.purchase.PurchaseAuditDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseBuyInfoDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseCheckDTO;
import com.zeroone.star.project.dto.j3.purchase.RevisePurchaseDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.purchase.DO.BuyDO;
import com.zeroone.star.purchase.DO.BuyInfoDO;
import com.zeroone.star.purchase.mapper.AccountMapper;
import com.zeroone.star.purchase.mapper.BuyInfoMapper;
import com.zeroone.star.purchase.mapper.BuyMapper;
import com.zeroone.star.purchase.mapper.RoomMapper;
import com.zeroone.star.purchase.service.IBuyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.alibaba.druid.util.StringUtils.*;

/**
 * <p>
 * 采购单 服务实现类
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
@Slf4j
@Service("purchaseIBuyService")
public class BuyServiceImpl extends ServiceImpl<BuyMapper, BuyDO> implements IBuyService {
    @Resource
    private BuyMapper buyMapper;
    @Resource
    private MsPurchaseMapper msPurchaseMapper;
    @Resource
    private IBuyInfoService buyInfoService;

    // 日期格式化器，用于将日期转换为"yyyy-MM-dd"格式的字符串
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 导出采购单列表到Excel流
     * author: Hazenix 
     */
    public void exportPurchaseNoteStreaming(List<String> ids, ByteArrayOutputStream finalOutputStream) {
        // 1. 查询采购单数据
        List<BuyDO> buyDOS = buyMapper.selectBatchIds(ids);
        // 2. 转换为Excel导出模型列表
        List<BuySimpleExcel> buySimpleExcels = msPurchaseMapper.buysToBuyExcels(buyDOS);

        // 3. 计算各金额字段的总和（实际金额、单据付款、核销金额、单据费用）
        BigDecimal totalActual = sumField(buySimpleExcels, BuySimpleExcel::getActual);
        BigDecimal totalMoney = sumField(buySimpleExcels, BuySimpleExcel::getMoney);
        BigDecimal totalWriteOff = sumField(buySimpleExcels, BuySimpleExcel::getWriteOff);
        BigDecimal totalCost = sumField(buySimpleExcels, BuySimpleExcel::getCost);

        // 4. 使用临时流生成Excel并处理格式
        try (ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream()) {
            // 4.1 用EasyExcel生成基础Excel（包含数据，无样式）
            EasyExcel.write(tmpBaos, BuySimpleExcel.class).sheet("采购单").doWrite(buySimpleExcels);

            // 4.2 处理Excel字节流，添加样式、标题、汇总行等
            byte[] processed = ExcelHelper.processEasyExcelBytes(tmpBaos.toByteArray(), workbook -> {
                Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
                Row origHeaderRow = sheet.getRow(0); // 获取原始表头行（EasyExcel生成的表头）
                int lastCellNum = (origHeaderRow == null) ? 0 : origHeaderRow.getLastCellNum(); // 计算列数
                if (lastCellNum <= 0) return; // 无数据则直接返回

                // 向下移动所有行（留出1行用于添加标题）
                sheet.shiftRows(0, sheet.getLastRowNum(), 1, true, false);

                // 创建各种样式（标题样式、表头样式、数据样式、汇总行样式）
                CellStyle titleStyle = ExcelStyleUtil.createTitleStyle(workbook);
                CellStyle headerStyle = ExcelStyleUtil.createHeaderStyle(workbook);
                CellStyle dataStyle = ExcelStyleUtil.createDataStyle(workbook);
                XSSFCellStyle summaryStyle = ExcelStyleUtil.createLightGrayStyle(workbook);

                // 写入标题行（第一行）
                ExcelHelper.writeTitle(sheet, "采购单列表", lastCellNum, titleStyle);
                // 为表头行（原表头行被下移到第2行）应用表头样式
                ExcelHelper.applyStyleToRow(sheet.getRow(1), headerStyle, lastCellNum);

                // 为数据行（从第3行开始）应用数据样式
                for (int r = 2; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) continue;
                    ExcelHelper.applyStyleToRow(row, dataStyle, lastCellNum);
                }

                // 定义金额格式化函数：空值转"0"，非空则去除末尾0并转为字符串
                java.util.function.Function<java.math.BigDecimal, String> fmt = bd -> {
                    if (bd == null) return "0";
                    java.math.BigDecimal v = bd.stripTrailingZeros();
                    return v.toPlainString();
                };
                // 准备汇总信息
                List<String> sums = Arrays.asList(
                        "总实际金额：" + fmt.apply(totalActual),
                        "总单据付款：" + fmt.apply(totalMoney),
                        "总核销金额：" + fmt.apply(totalWriteOff),
                        "总单据费用：" + fmt.apply(totalCost)
                );
                // 写入汇总行到表格末尾
                ExcelHelper.writeSummaryRow(sheet, lastCellNum, sums, summaryStyle);
                // 调整列宽
                ExcelHelper.scaleColumnWidths(sheet, lastCellNum, 3, 16);
            });

            // 5. 将处理后的Excel字节写入最终输出流
            finalOutputStream.write(processed);
            finalOutputStream.flush();

        } catch (IOException ex) {
            throw new RuntimeException("导出采购单报表失败", ex);
        }
    }

    /**
     * 对列表中对象的特定字段求和（支持BigDecimal类型）
     * @param list 数据列表
     * @param fieldExtractor 字段提取器，用于从对象中获取需要求和的字段
     * @return 字段总和（空列表或空字段返回0）
     */
    private BigDecimal sumField(List<BuySimpleExcel> list, Function<BuySimpleExcel, BigDecimal> fieldExtractor) {
        if (list == null) return BigDecimal.ZERO;
        // 流式处理：提取字段 -> 过滤空值 -> 累加求和
        return list.stream().map(fieldExtractor).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 导出采购单详情到ZIP流（每个采购单生成一个Excel文件，打包到ZIP）
     * author: Hazenix 
     */
    public void exportPurchaseNoteDetailStreaming(List<String> ids, ByteArrayOutputStream baos) {
        // 1. 查询采购单主表数据和详情数据
        List<BuyDO> buyDOS = buyMapper.selectBatchIds(ids);
        List<BuyInfoDO> buyInfoDOS = buyInfoService.selectBatchByPids(ids);
        // 2. 转换详情数据为Excel导出模型
        List<BuyInfoDetailExcel> buyInfoDetailExcels = msPurchaseMapper.buyInfosToBuyInfoDetailExcels(buyInfoDOS);

        // 3. 按采购单ID分组详情数据（便于按单生成Excel）
        Map<String, List<BuyInfoDetailExcel>> infoByPid = ExcelHelper.groupDetailsByPid(buyInfoDOS, buyInfoDetailExcels);

        // 4. 创建ZIP输出流，将多个Excel打包
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            int idx = 1; // 用于生成默认文件名（当采购单编号为空时）
            for (BuyDO buy : buyDOS) {
                if (buy == null) continue; // 跳过空数据
                String pid = buy.getId();
                // 获取当前采购单的详情列表（无详情则返回空列表）
                List<BuyInfoDetailExcel> details = infoByPid.getOrDefault(pid, Collections.emptyList());

                // 生成Excel文件名（基于采购单编号，处理特殊字符）
                String baseName = buy.getNumber();
                if (baseName == null || baseName.trim().isEmpty()) baseName = pid;
                if (baseName == null || baseName.trim().isEmpty()) baseName = String.valueOf(idx);
                // 替换文件名中的特殊字符（避免ZIP条目名称异常）
                String safeBaseName = baseName.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
                String entryName = safeBaseName + ".xlsx";

                // 5. 为当前采购单生成Excel
                try (ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream()) {
                    // 工作表名称（过长则截断）
                    String sheetName = "采购单-" + (safeBaseName.length() > 20 ? safeBaseName.substring(0, 20) : safeBaseName);
                    // 用EasyExcel生成基础Excel（包含详情数据）
                    EasyExcel.write(tmpBaos, BuyInfoDetailExcel.class).sheet(sheetName).doWrite(details);

                    // 6. 处理Excel格式：添加标题、采购单信息、样式等
                    byte[] processed = ExcelHelper.processEasyExcelBytes(tmpBaos.toByteArray(), workbook -> {
                        Sheet sheet = workbook.getSheetAt(0);
                        Row origHeaderRow = sheet.getRow(0);
                        int lastCellNum = (origHeaderRow == null) ? 0 : origHeaderRow.getLastCellNum();
                        if (lastCellNum <= 0) return;

                        // 向下移动所有行（留出2行：1行标题 + 1行采购单基本信息）
                        sheet.shiftRows(0, sheet.getLastRowNum(), 2, true, false);

                        // 创建样式
                        CellStyle titleStyle = ExcelStyleUtil.createTitleStyle(workbook);
                        CellStyle headerStyle = ExcelStyleUtil.createHeaderStyle(workbook);
                        CellStyle dataStyle = ExcelStyleUtil.createDataStyle(workbook);
                        XSSFCellStyle infoStyle = ExcelStyleUtil.createLightGrayStyle(workbook);

                        // 写入标题行（第一行）
                        ExcelHelper.writeTitle(sheet, "采购单", lastCellNum, titleStyle);

                        // 创建采购单基本信息行（第二行）
                        Row buyInfoRow = sheet.createRow(1);
                        // 供应商信息单元格
                        Cell supCell = buyInfoRow.createCell(0);
                        supCell.setCellValue("供应商：" + Optional.ofNullable(buy.getSupplier()).orElse(""));
                        supCell.setCellStyle(infoStyle);

                        // 单据日期单元格（格式化日期）
                        String dateStr = "";
                        if (buy.getTime() != null) {
                            try {
                                dateStr = buy.getTime().format(DTF);
                            } catch (Exception ignored) {
                                dateStr = buy.getTime().toString(); // 格式化失败则直接转字符串
                            }
                        }
                        Cell dateCell = buyInfoRow.createCell(1);
                        dateCell.setCellValue("单据日期：" + dateStr);
                        dateCell.setCellStyle(infoStyle);

                        // 单据编号单元格（合并单元格显示，避免过长）
                        int mergeStart = 2;
                        int mergeEnd = Math.min(3, lastCellNum - 1); // 最多合并到第3列（避免越界）
                        Cell billNoCell = buyInfoRow.createCell(mergeStart);
                        billNoCell.setCellValue("单据编号：" + Optional.ofNullable(buy.getNumber()).orElse(""));
                        billNoCell.setCellStyle(infoStyle);
                        if (mergeEnd >= mergeStart) {
                            // 合并单元格（行1，列mergeStart到mergeEnd）
                            sheet.addMergedRegion(new CellRangeAddress(1, 1, mergeStart, mergeEnd));
                        }

                        // 为采购单信息行应用样式
                        ExcelHelper.applyStyleToRow(buyInfoRow, infoStyle, lastCellNum);
                        // 为表头行（原表头被下移到第3行）应用表头样式
                        ExcelHelper.applyStyleToRow(sheet.getRow(2), headerStyle, lastCellNum);

                        // 为数据行（从第4行开始）应用数据样式（跳过空行）
                        for (int r = 3; r <= sheet.getLastRowNum(); r++) {
                            Row row = sheet.getRow(r);
                            if (row == null) continue;
                            boolean hasData = false;
                            // 判断行是否有有效数据
                            for (int c = 0; c < lastCellNum; c++) {
                                String text = readCellAsString(row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                                if (text != null && !text.trim().isEmpty()) {
                                    hasData = true;
                                    break;
                                }
                            }
                            if (!hasData) continue;
                            ExcelHelper.applyStyleToRow(row, dataStyle, lastCellNum);
                        }

                        // 7. 添加采购单额外信息行（在数据行下方）
                        int afterDataRowIndex = sheet.getLastRowNum() + 1;
                        Row extraRow = sheet.createRow(afterDataRowIndex);
                        // 额外信息的标签和值
                        List<String> labels = Arrays.asList("单据金额", "单据费用", "实际金额", "核销金额", "结算账户", "发票信息", "关联人员", "物流信息", "备注信息");
                        List<String> values = Arrays.asList(
                                Optional.ofNullable(buy.getTotal()).map(BigDecimal::toPlainString).orElse("0"),
                                Optional.ofNullable(buy.getCost()).map(BigDecimal::toPlainString).orElse("0"),
                                Optional.ofNullable(buy.getActual()).map(BigDecimal::toPlainString).orElse("0"),
                                Optional.ofNullable(buy.getMoney()).map(BigDecimal::toPlainString).orElse("0"),
                                Optional.ofNullable(buy.getAccount()).orElse(""),
                                Optional.ofNullable(buy.getInvoice()).map(i -> Integer.toString(i)).orElse("0"),
                                Optional.ofNullable(buy.getPeople()).orElse(""),
                                Optional.ofNullable(buy.getLogistics()).orElse(""),
                                Optional.ofNullable(buy.getData()).orElse("")
                        );
                        // 写入额外信息到单元格
                        for (int c = 0; c < lastCellNum; c++) {
                            Cell cell = extraRow.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            if (cell == null) cell = extraRow.createCell(c);
                            if (c < labels.size()) {
                                cell.setCellValue(labels.get(c) + "：" + values.get(c));
                            } else {
                                cell.setCellValue(""); // 超出标签数量的列留空
                            }
                            cell.setCellStyle(infoStyle);
                        }

                        // 调整列宽
                        ExcelHelper.scaleColumnWidths(sheet, lastCellNum, 3, 16);
                    });

                    // 8. 将处理后的Excel写入ZIP流
                    try (ByteArrayOutputStream finalExcelBaos = new ByteArrayOutputStream()) {
                        finalExcelBaos.write(processed);
                        byte[] excelBytes = finalExcelBaos.toByteArray();
                        ZipEntry zipEntry = new ZipEntry(entryName); // 创建ZIP条目
                        zos.putNextEntry(zipEntry);
                        zos.write(excelBytes); // 写入Excel字节
                        zos.closeEntry(); // 关闭当前ZIP条目
                    }
                } catch (IOException e) {
                    throw new RuntimeException("生成单个 Excel 失败: " + entryName, e);
                }
                idx++;
            }
            zos.finish(); // 完成ZIP打包
        } catch (IOException e) {
            throw new RuntimeException("导出 detail zip 失败", e);
        }
    }

    /**
     * 将Excel单元格内容转换为字符串（处理不同类型的单元格）
     * @param cell 单元格对象
     * @return 单元格内容的字符串表示（空单元格返回空串）
     */
    private static String readCellAsString(Cell cell) {
        if (cell == null) return "";
        try {
            // 根据单元格类型处理
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    // 日期类型单元格特殊处理
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue().toString();
                    }
                    // 数字类型转换为BigDecimal避免科学计数法
                    double d = cell.getNumericCellValue();
                    BigDecimal bd = BigDecimal.valueOf(d);
                    return bd.stripTrailingZeros().toPlainString();
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    // 公式单元格先尝试读字符串，失败则读数字
                    try {
                        return cell.getStringCellValue();
                    } catch (Exception ex) {
                        try {
                            return String.valueOf(cell.getNumericCellValue());
                        } catch (Exception ex2) {
                            return "";
                        }
                    }
                default:
                    return "";
            }
        } catch (Exception ex) {
            return ""; // 任何异常都返回空串
        }
    }

    /**
     * 导入采购单
     * @param excelFile
     * author: Hazenix 
     */
// 批量写入阈值（可调）
    private static final int BATCH_SAVE_SIZE = 500;

    /**
     * 主入口：接收 MultipartFile（前端上传），直接用 InputStream 读取
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonVO<String> importPurchaseNote(MultipartFile file) throws IOException {
        try (InputStream in = file.getInputStream()) {
            final int HEAD_ROW_NUMBER = 2;

            // 读取Excel数据
            List<CombinedRowExcel> rows = EasyExcel.read(in)
                    .head(CombinedRowExcel.class)
                    .sheet()
                    .headRowNumber(HEAD_ROW_NUMBER)
                    .doReadSync();

            if (rows == null || rows.isEmpty()) {
                throw new RuntimeException("Excel 中无数据行");
            }

            // 检验单据必填数据
            CombinedRowExcel firstDataRow = rows.get(0);
            List<String> buyLevelErrors = validateBuyLevel(firstDataRow);
            if (!buyLevelErrors.isEmpty()) {
                String msg = "单据级字段缺失或格式错误: " + String.join(", ", buyLevelErrors);
                return JsonVO.fail(msg);
            }

            // 1) 构建并保存采购单主信息
            BuyDO buy = buildBuyFromCombined(firstDataRow);
            buyMapper.insert(buy);

            // 2) 构建并批量保存采购单商品信息
            List<BuyInfoDO> saveList = new ArrayList<>();
            for (CombinedRowExcel r : rows) {
                // 检验商品必填数据
                List<String> prodErr = validateProductLevel(r);
                if (!prodErr.isEmpty()) {
                    log.error("跳过商品: {}，原因：{}", r.getProductName(), String.join(";", prodErr));
                    continue;
                }

                BuyInfoDO info = buildBuyInfoFromCombined(r);
                if (info != null) {
                    info.setPid(buy.getId());
                    info.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                }
                saveList.add(info);

                // 批量保存
                if (saveList.size() >= BATCH_SAVE_SIZE) {
                    buyInfoService.saveBatch(saveList);
                    saveList.clear();
                }
            }
            // 保存剩余数据
            if (!saveList.isEmpty()) {
                buyInfoService.saveBatch(saveList);
            }
        }
        return JsonVO.success("ok");
    }


    // -------------------- DTO -> DO 映射方法 --------------------

    /**
     * 构建 buyDO
     */
    private BuyDO buildBuyFromCombined(CombinedRowExcel r) {
        BuyDO buy = new BuyDO();
        // 基础字段映射
        buy.setSupplier(ParseUtils.trimToNull(r.getSupplier()));
        buy.setNumber(ParseUtils.trimToNull(r.getBillNumber()));

        if (org.springframework.util.StringUtils.hasText(r.getBillDate())) {
            buy.setTime(ParseUtils.parseToLocalDateTime(r.getBillDate()));
        }

        // 金额字段映射
        buy.setTotal(ParseUtils.parseToBigDecimalSafe(r.getBillTotal()));
        buy.setActual(ParseUtils.parseToBigDecimalSafe(r.getActualAmount()));
        buy.setMoney(ParseUtils.parseToBigDecimalSafe(r.getPaidAmount()));

        buy.setAccount(ParseUtils.trimToNull(r.getAccount()));
        buy.setPeople(ParseUtils.trimToNull(r.getRelatedPeople()));
        buy.setLogistics(ParseUtils.trimToNull(r.getLogistics()));
        buy.setData(ParseUtils.trimToNull(r.getHeaderRemark()));

        // 非空字段默认值处理
        if (buy.getId() == null || buy.getId().trim().isEmpty()) {
            buy.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        if (buy.getFrame() == null || buy.getFrame().trim().isEmpty()) {
            buy.setFrame("0");
        }
        if (buy.getCost() == null) {
            buy.setCost(new BigDecimal("0.0000"));
        }
        if (buy.getExamine() == null) {
            buy.setExamine(0);
        }
        if (buy.getNucleus() == null) {
            buy.setNucleus(0);
        }
        if (buy.getCse() == null) {
            buy.setCse(0);
        }
        if (buy.getInvoice() == null) {
            buy.setInvoice(0);
        }
        if (buy.getCheck() == null) {
            buy.setCheck(false);
        }
        if (buy.getUser() == null || buy.getUser().trim().isEmpty()) {
            buy.setUser("test_creator_" + System.currentTimeMillis());
        }

        return buy;
    }

    /**
     * 构建 BuyInfoDO
     */
    private BuyInfoDO buildBuyInfoFromCombined(CombinedRowExcel r) {
        String goods = ParseUtils.trimToNull(r.getProductName());
        if (!org.springframework.util.StringUtils.hasText(goods)) {
            return null; // 无商品名则跳过
        }

        BuyInfoDO info = new BuyInfoDO();
        info.setGoods(ParseUtils.truncate(goods, 32));
        info.setAttr(ParseUtils.truncate(ParseUtils.trimToNull(r.getAttr()), 64));
        info.setUnit(ParseUtils.truncate(ParseUtils.trimToNull(r.getUnit()), 32));
        info.setWarehouse(ParseUtils.trimToNull(r.getWarehouse()));
        info.setBatch(ParseUtils.trimToNull(r.getBatchNo()));

        if (org.springframework.util.StringUtils.hasText(r.getProductionDate())) {
            info.setMfd(LocalDate.from(ParseUtils.parseToLocalDateTime(r.getProductionDate())));
        }

        // 价格、数量等字段映射
        info.setPrice(ParseUtils.parseToBigDecimalSafe(r.getUnitPrice()));
        info.setNums(ParseUtils.parseToBigDecimalSafe(r.getQuantity()));
        info.setSerial(ParseUtils.trimToNull(r.getSerialNo()));

        // 折扣、税率等字段映射
        info.setDiscount(ParseUtils.parsePercentOrNumber(r.getDiscountRate()));
        info.setDsc(ParseUtils.parseToBigDecimalSafe(r.getDiscountAmount()));
        info.setTotal(ParseUtils.parseToBigDecimalSafe(r.getItemTotal()));
        info.setTax(ParseUtils.parsePercentOrNumber(r.getTaxRate()));
        info.setTat(ParseUtils.parseToBigDecimalSafe(r.getTaxAmount()));
        info.setTpt(ParseUtils.parseToBigDecimalSafe(r.getTotalWithTax()));

        info.setData(ParseUtils.trimToNull(r.getProductRemark()));

        // 非空字段默认值处理
        info.setRetreat(info.getRetreat() == null ? BigDecimal.ZERO : info.getRetreat());
        info.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        info.setPid("test_buy_id_" + System.currentTimeMillis()); // 实际应关联采购单ID

        // 补充默认值
        if (info.getDiscount() == null) info.setDiscount(new BigDecimal("0.00"));
        if (info.getDsc() == null) info.setDsc(new BigDecimal("0.0000"));
        if (info.getTax() == null) info.setTax(new BigDecimal("0.00"));
        if (info.getTat() == null) info.setTat(new BigDecimal("0.0000"));
        if (info.getPrice() == null) info.setPrice(new BigDecimal("0.0000"));
        if (info.getNums() == null) info.setNums(new BigDecimal("1.0000"));
        if (info.getTotal() == null) info.setTotal(new BigDecimal("0.0000"));
        if (info.getTpt() == null) info.setTpt(new BigDecimal("0.0000"));

        return info;
    }

    // -------------------- 校验方法 --------------------

    /**
     * 检验buy级必填字段
     */
    private List<String> validateBuyLevel(CombinedRowExcel r) {
        List<String> errs = new ArrayList<>();
        if (!org.springframework.util.StringUtils.hasText(r.getSupplier())) errs.add("供应商");
        if (!org.springframework.util.StringUtils.hasText(r.getBillDate())) errs.add("单据日期");
        if (!org.springframework.util.StringUtils.hasText(r.getBillNumber())) errs.add("单据编号");
        if (!org.springframework.util.StringUtils.hasText(r.getActualAmount())) errs.add("实际金额");
        if (!org.springframework.util.StringUtils.hasText(r.getPaidAmount())) errs.add("实付金额");
        if (!org.springframework.util.StringUtils.hasText(r.getAccount())) errs.add("结算账户");
        return errs;
    }

    /**
     * 检验buyInfo级必填字段
     */
    private List<String> validateProductLevel(CombinedRowExcel r) {
        List<String> errs = new ArrayList<>();
        if (!org.springframework.util.StringUtils.hasText(r.getProductName())) errs.add("商品名称");
        if (!org.springframework.util.StringUtils.hasText(r.getWarehouse())) errs.add("仓库");
        if (!org.springframework.util.StringUtils.hasText(r.getUnitPrice())) errs.add("单价");
        if (!org.springframework.util.StringUtils.hasText(r.getQuantity())) errs.add("数量");
        if (!org.springframework.util.StringUtils.hasText(r.getDiscountRate())) errs.add("折扣率(%)");
        if (!org.springframework.util.StringUtils.hasText(r.getTaxRate())) errs.add("税率(%)");
        return errs;
    }


    @Autowired
    private BuyInfoMapper buyInfoMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private AccountMapper accountMapper;

    /** 单条审核事务执行器 */
    @Autowired
    private PurchaseAuditExecutor purchaseAuditExecutor;

    /** Redisson 分布式锁客户端 */
    @Autowired
    private org.redisson.api.RedissonClient redissonClient;
    /**
     * @Description: 修改采购单
     * @Author: 正念
     */
    @Override
    public JsonVO<String> updateBuy(RevisePurchaseDTO revisePurchaseDTO) {
        //把前端接受的数据依次放到对应DO中去
        BuyDO buyDO = BuyDO.builder()
                .id(revisePurchaseDTO.getId())
                .supplier(revisePurchaseDTO.getSupplier())
                .time(revisePurchaseDTO.getTime())
                .number(revisePurchaseDTO.getNumber())
                .actual(revisePurchaseDTO.getActual())
                .money(revisePurchaseDTO.getMoney())
                .account(revisePurchaseDTO.getAccount())
                .people(revisePurchaseDTO.getPeople())
                .logistics(revisePurchaseDTO.getLogistics())
                .file(revisePurchaseDTO.getFile())
                .data(revisePurchaseDTO.getData())
                .examine(revisePurchaseDTO.getExamine())
                .check(revisePurchaseDTO.getCheckStatus()==0?false:true)
                .build();
        //前端获取的
        int examineTemp = revisePurchaseDTO.getExamine();
        //数据库中的
        int examineSQL = buyMapper.selectById(revisePurchaseDTO.getId()).getExamine();
        int rows = buyMapper.updateByPurchaseId(buyDO);
        if( rows==0){
            return JsonVO.fail("更新失败,采购单记录不存在");
        }
        List<PurchaseBuyInfoDTO> details = revisePurchaseDTO.getDetails();
        //需要判断该条数据是否之前存在过，存在则修改，不存在就新增
        if(details!=null && !details.isEmpty()){
            for (PurchaseBuyInfoDTO detail : details) {
                String id = detail.getId();
                //折扣额
                BigDecimal dsc = detail.getPrice()
                        .multiply(detail.getDiscount())
                        .multiply(detail.getNums())
                        .multiply(BigDecimal.valueOf(0.01))
                        .setScale(4, RoundingMode.HALF_UP);
                //金额
                BigDecimal total = detail.getPrice()
                        .multiply(detail.getNums())
                        .subtract(dsc)
                        .setScale(4, RoundingMode.HALF_UP);
                //税额
                BigDecimal tat = total
                        .multiply(detail.getTax())
                        .multiply(BigDecimal.valueOf(0.01))
                        .setScale(4, RoundingMode.HALF_UP);
                //价税合计
                BigDecimal tpt = tat.add(total)
                        .setScale(4, RoundingMode.HALF_UP);
                BuyInfoDO buyInfoDO = BuyInfoDO.builder()
                        .id(detail.getId())
                        .pid(revisePurchaseDTO.getId())
                        .goods(detail.getGoods())
                        .unit(detail.getUnit())
                        .warehouse(detail.getWarehouse())
                        .price(detail.getPrice())
                        .nums(detail.getNums())
                        .discount(detail.getDiscount())
                        .dsc(dsc)
                        .total(total)
                        .tax(detail.getTax())
                        .tat(tat)
                        .tpt(tpt)
                        .data(detail.getData())
                        .retreat(detail.getRetreat())
                        .build();
                if(buyInfoMapper.selectById(buyInfoDO.getId()) == null){
                    rows = buyInfoMapper.insert(buyInfoDO);
                    if( rows==0){
                        return JsonVO.fail("更新失败,采购单详情插入失败");
                    }
                }
                else {
                    rows = buyInfoMapper.updateById(buyInfoDO);
                    if( rows==0){
                        return JsonVO.fail("更新失败,采购单详情更新失败");
                    }
                }
                //审核反审核相应的库存更新
                if(examineTemp!=examineSQL){
                    //需要去回档库存
                    if(examineTemp==0){
                        roomMapper.updateNumsByWarehouseAndGoods(detail.getWarehouse(), detail.getGoods(),detail.getNums());
                    } else if (examineTemp==1) {
                        //去消耗库存
                        roomMapper.updateNumsByWarehouseAndGoods(detail.getWarehouse(), detail.getGoods(),detail.getNums().multiply(BigDecimal.valueOf(-1)));

                    }
                    else{return JsonVO.fail("更新失败,审核数据不对");}
                }
            }
        }
         List<String> incomingIds = details.stream()
                .map(d -> org.springframework.util.StringUtils.hasText(d.getId()) ? d.getId() : null)
                .filter(Objects::nonNull).collect(Collectors.toList());
         buyInfoMapper.deleteByPidAndIdNotIn(revisePurchaseDTO.getId(), incomingIds);

        //审核反审核相应的资金更新
        if(examineTemp!=examineSQL){
            //需要去回档资金
            if(examineTemp==0){
                accountMapper.updateAccountByName(revisePurchaseDTO.getAccount(),revisePurchaseDTO.getMoney().multiply(BigDecimal.valueOf(-1)));
            } else if (examineTemp==1) {
                //去增加资金
                accountMapper.updateAccountByName(revisePurchaseDTO.getAccount(),revisePurchaseDTO.getMoney());
            }
            else{return JsonVO.fail("更新失败,审核数据不对");}
        }
        return JsonVO.success("更新成功");
    }

    /**
     * @Description: 核对/反核对（批量）
     * @Author: 正念
     */
    @Override
    public JsonVO<String> checkStatus(PurchaseCheckDTO checkList) {

        List<String> ids = checkList.getIds();
        int type = checkList.getType();
        for (String id : ids) {
            int row = buyMapper.updateCheckById(id,type);
            if(row==0){
                return JsonVO.fail("修改失败");
            }
        }
        return JsonVO.success("修改成功");
    }

    /**
     * 审核/反审核（批量）
     *
     * <p>防重方案：
     * <ol>
     *   <li><b>Redisson 分布式锁</b>（key = {@code purchase:audit:lock:{id}}，超时 30 s）
     *       — 保证同一张采购单在任意时刻只有一个线程可进入审核流程，防止并发重复提交。</li>
     *   <li><b>幂等检查</b>（在 {@link PurchaseAuditExecutor} 内，锁内读取最新 examine 值）
     *       — 即使绕过锁也无法二次变更，杜绝资金/库存双倍操作。</li>
     *   <li><b>事务隔离</b>（每条单据独立 {@code @Transactional}，提交后才释放锁）
     *       — 后续线程获锁后必然读到已提交状态，消除"读后写"竞态窗口。</li>
     * </ol>
     *
     * @param auditList 批量审核参数：ids + type（1=审核，0=反审核）
     */
    @Override
    public JsonVO<String> examineStatus(PurchaseAuditDTO auditList) {
        List<String> ids = auditList.getIds();
        if (ids == null || ids.isEmpty()) {
            return JsonVO.fail("采购单ID列表不能为空");
        }

        int target = auditList.getType(); // 1=审核，0=反审核
        int changed = 0;
        List<String> skipped = new java.util.ArrayList<>();

        for (String id : ids) {
            // ── 1. 获取 Redisson 分布式锁（粒度：单据ID）────────────────────────────
            org.redisson.api.RLock lock =
                    redissonClient.getLock("purchase:audit:lock:" + id);
            boolean locked = false;
            try {
                // 等待最多 3 秒抢锁，持锁最长 30 秒
                locked = lock.tryLock(3, 30, java.util.concurrent.TimeUnit.SECONDS);
                if (!locked) {
                    log.warn("[审核] 采购单 [{}] 正被其他用户处理，本次跳过", id);
                    skipped.add(id);
                    continue;
                }

                // ── 2. 委托执行器完成事务（库存 + 资金 + examine 字段，提交后返回）──
                boolean ok = purchaseAuditExecutor.execute(id, target);
                if (ok) {
                    changed++;
                } else {
                    log.info("[审核] 采购单 [{}] 无需变更（状态一致或数据异常）", id);
                }

            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.error("[审核] 采购单 [{}] 获取分布式锁被中断", id, ie);
                return JsonVO.fail("系统繁忙，请稍后重试");
            } catch (Exception e) {
                log.error("[审核] 采购单 [{}] 处理异常: {}", id, e.getMessage(), e);
                return JsonVO.fail("处理采购单 [" + id + "] 时出错: " + e.getMessage());
            } finally {
                // ── 3. 事务提交后释放锁，保证后续线程读到已提交状态 ────────────────
                if (locked && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }

        if (changed == 0 && skipped.isEmpty()) {
            return JsonVO.fail("没有任何采购单被修改，请检查ID是否正确或状态是否已一致");
        }

        String msg = "审核/反审核完成，共修改 " + changed + " 条";
        if (!skipped.isEmpty()) {
            msg += "；以下单据被跳过（并发冲突）：" + skipped;
        }
        return JsonVO.success(msg);
    }

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private BorInfoMapper borInfoMapper;

    /**
     * 获取生成采购单数据
     * @Author 待兼唐怀瑟
     * @param id
     * @return
     */
    @Override
    public PurchaseNoteGenerateDataDTO generatePurchaseData(String id) {
        // bor_info.id定位
        BuyInfoDO info = buyInfoMapper.selectById(id);
        if (info == null) throw new RuntimeException("明细行不存在");

        // 拿到 buy抬头
        BuyDO buy = buyMapper.selectById(info.getPid());
        if (buy == null || !Boolean.TRUE.equals(buy.getExamine())) {
            throw new RuntimeException("主单不存在或未审核");
        }

        GoodsDO g = goodsMapper.selectById(info.getGoods());

        // 构造返回 DTO
        PurchaseNoteGenerateDataDTO dto = new PurchaseNoteGenerateDataDTO();

        // 明细字段
        dto.setGoods(info.getGoods());
        dto.setName(g == null ? "" : g.getName());
        dto.setSpec(g == null ? "" : g.getSpec());
        dto.setAttr(info.getAttr());
        dto.setWarehouse(info.getWarehouse());
        dto.setUnit(info.getUnit());
        dto.setBatch(info.getBatch());
        dto.setPrice(info.getPrice().toPlainString());
        dto.setNums(info.getNums().toPlainString());
        dto.setAmount(info.getTotal().toPlainString());
        dto.setTax(info.getTax().toPlainString());
        dto.setTat(info.getTat().toPlainString());
        dto.setTpt(info.getTpt().toPlainString());
        dto.setData(info.getData());

        // 抬头字段
        PurchaseNoteOtherGenerateDataDTO header = new PurchaseNoteOtherGenerateDataDTO();
        header.setSupplier(buy.getSupplier());
        header.setTime(buy.getTime().toLocalDate().toString());
        header.setNumber(buy.getNumber());
        header.setTotal(buy.getTotal().toPlainString());
        header.setActual(buy.getActual().toPlainString());
        header.setMoney(buy.getMoney() == null ? "" : buy.getMoney().toPlainString());
        header.setCost(buy.getCost() == null ? "" : buy.getCost().toPlainString());
        header.setAccount(buy.getAccount());
        header.setPeople(buy.getPeople());
        header.setLogistics(buy.getLogistics());
        header.setFile(buy.getFile());
        header.setData(buy.getData());

        dto.setPurchaseNoteOtherGenerateDataDTO(header);
        return dto;
    }

    /**
     * 获取采购单列表实现类
     * @param query 查询条件
     * @return
     * @author Hazenix 
     */
    @Override
    public PageDTO<PurchaseNoteListDTO> listPurchaseNote(PurchaseNoteQuery query){
        //创建分页对象
        Page<BuyVO> page = new Page<>(query.getPageIndex(), query.getPageSize());

        //构建查询条件
        LambdaQueryWrapper<BuyDO> queryWrapper = new LambdaQueryWrapper<>();

        //查询条件
        //商品名称留空（已经在xml文件中处理）
        //单据编号
        if(query.getNumber() != null && !query.getNumber().isEmpty()) {
            queryWrapper.like(BuyDO::getNumber, query.getNumber());
        }
        //备注信息
        if (query.getData() != null && !query.getData().isEmpty()){
            queryWrapper.like(BuyDO::getData, query.getData());
        }
        //供应商名称
        if (query.getSupplier() != null && !query.getSupplier().isEmpty()){
            queryWrapper.like(BuyDO::getSupplier, query.getSupplier());
        }
        //制单人
        if (query.getNoteUser() != null && !query.getNoteUser().isEmpty()){
            queryWrapper.like(BuyDO::getUser, query.getNoteUser());
        }
        //关联人员
        if (query.getPeople() != null && !query.getPeople().isEmpty()){
            queryWrapper.like(BuyDO::getPeople, query.getPeople());
        }

        //审核状态
        if (query.getExamine() != null){
            queryWrapper.eq(BuyDO::getExamine, query.getExamine());
        }
        //核销状态
        if (query.getNucleus() != null){
            queryWrapper.eq(BuyDO::getNucleus, query.getNucleus());
        }
        //费用状态
        if (query.getCse() != null){
            queryWrapper.eq(BuyDO::getCse, query.getCse());
        }
        //发票状态
        if (query.getInvoice() != null){
            queryWrapper.eq(BuyDO::getInvoice, query.getInvoice());
        }
        //核销状态
        if (query.getCheckStatus() != null){
            queryWrapper.eq(BuyDO::getCheck, query.getCheckStatus());
        }

        //开始时间
        if (query.getStartTime() != null && !query.getStartTime().isEmpty()){
            //YYYY-MM-DD
            queryWrapper.ge(BuyDO::getTime, query.getStartTime());
        }
        //结束时间
        if (query.getEndTime() != null && !query.getEndTime().isEmpty()){
            queryWrapper.le(BuyDO::getTime, query.getEndTime());
        }

        //执行分页查询
        Page<BuyVO> resultPage = this.baseMapper.selectPageWithProduct(page, queryWrapper, query);

        //转化你为PageDTO返回
        return PageDTO.create(resultPage, PurchaseNoteListDTO.class);
    }

}
