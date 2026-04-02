package com.zeroone.star.fund.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeroone.star.common.exception.BusinessException;
import com.zeroone.star.fund.entity.Receipt;
import com.zeroone.star.fund.entity.ReceiptDetail;
import com.zeroone.star.fund.mapper.ReceiptDetailMapper;
import com.zeroone.star.fund.mapper.ReceiptMapper;
import com.zeroone.star.fund.service.FundReceiptService;
import com.zeroone.star.project.dto.j4.fund.ReceiptAddDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptAuditDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptDeleteDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptSettlementDetailDTO;
import com.zeroone.star.project.vo.fund.ImportErrorVO;
import com.zeroone.star.project.vo.fund.ImportResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.zeroone.star.project.dto.j4.fund.ReceiptUpdateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


/**
 * <p>
 * 描述：资金管理-收款单服务实现类
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author ikun
 * @version 1.0.0
 */
@Slf4j
@Service
public class FundReceiptServiceImpl implements FundReceiptService {
    @Resource
    private ReceiptMapper receiptMapper;

    @Resource
    private ReceiptDetailMapper receiptDetailMapper;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReceipt(ReceiptAddDTO receiptAddDTO) {
        // 1. 保存收款单主表信息
        Receipt receipt = new Receipt();
        // 手动拷贝属性
        receipt.setCustomer(receiptAddDTO.getCustomer());
        receipt.setTime(receiptAddDTO.getTime());
        receipt.setNumber(receiptAddDTO.getNumber());
        receipt.setTotal(receiptAddDTO.getTotal());
        receipt.setPeople(receiptAddDTO.getPeople());
        receipt.setFile(receiptAddDTO.getFile());
        receipt.setData(receiptAddDTO.getData());
        receipt.setUser(receiptAddDTO.getUser());
        receipt.setFrame(receiptAddDTO.getFrame());

        try {
            if (receiptAddDTO.getInfo() != null) {
                String moreJson = objectMapper.writeValueAsString(receiptAddDTO.getInfo());
                receipt.setMore(moreJson);
            }
        } catch (JsonProcessingException e) {
            // 处理序列化异常
            e.printStackTrace();
        }

        // 生成主键ID
        String id = String.valueOf(IdWorker.getId());
        receipt.setId(id);
        
        // 设置默认值
        receipt.setExamine(0); // 未审核
        receipt.setNucleus(0); // 未核销
        
        // 2. 保存主表数据
        receiptMapper.insert(receipt);
        
        // 3. 保存收款单详情信息
        List<ReceiptSettlementDetailDTO> details = receiptAddDTO.getInfo();
        if (details != null && !details.isEmpty()) {
            for (ReceiptSettlementDetailDTO detail : details) {
                ReceiptDetail receiptDetail = new ReceiptDetail();
                // 拷贝属性
                receiptDetail.setAccount(detail.getAccount());
                receiptDetail.setMoney(detail.getAmount());
                receiptDetail.setSettle(detail.getSettle());
                receiptDetail.setData(detail.getData());
                
                // 生成主键ID
                receiptDetail.setId(String.valueOf(IdWorker.getId()));
                receiptDetail.setPid(id); // 关联主表ID

                receiptDetailMapper.insert(receiptDetail);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResultVO importReceiptData(MultipartFile file) {
        List<ImportErrorVO> errorList = new ArrayList<>();
        int successCount = 0;
        int totalCount = 0;
        long startTime = System.currentTimeMillis();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            totalCount = sheet.getLastRowNum(); // 获取总行数（排除标题行）

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    // 解析Excel行数据
                    ReceiptAddDTO receiptAddDTO = parseReceiptFromExcelRow(row);

                    // 保存数据
                    addReceipt(receiptAddDTO);
                    successCount++;

                } catch (Exception e) {
                    // 记录错误信息
                    ImportErrorVO error = new ImportErrorVO();
                    error.setRowNumber(i + 1); // Excel行号从1开始
                    error.setErrorMessage(e.getMessage());
                    error.setErrorType("数据导入失败");
                    errorList.add(error);
                    log.error("导入第{}行数据失败", i + 1, e);
                }
            }
        } catch (IOException e) {
            log.error("读取Excel文件失败", e);
            throw new RuntimeException("读取Excel文件失败");
        }

        long costTime = System.currentTimeMillis() - startTime;

        ImportResultVO result = new ImportResultVO();
        result.setTotalCount(totalCount);
        result.setSuccessCount(successCount);
        result.setFailureCount(errorList.size());
        result.setCostTime(costTime);
        result.setErrorList(errorList);
        result.setSummary(String.format("导入完成，共处理%d条记录，成功%d条，失败%d条",
                totalCount, successCount, errorList.size()));

        return result;
    }

    /**
     * 从Excel行解析收款单数据（包含结算详情）
     */
    private ReceiptAddDTO parseReceiptFromExcelRow(Row row) {
        ReceiptAddDTO dto = new ReceiptAddDTO();

        // 解析主表数据
        dto.setFrame(getCellStringValue(row.getCell(0)));           // 所属组织
        dto.setCustomer(getCellStringValue(row.getCell(1)));        // 客户
        dto.setTime(parseLocalDateTime(getCellStringValue(row.getCell(2)))); // 单据时间
        dto.setNumber(getCellStringValue(row.getCell(3)));          // 单据编号
        dto.setTotal(parseBigDecimal(getCellStringValue(row.getCell(4)))); // 单据金额
        dto.setPeople(getCellStringValue(row.getCell(5)));          // 关联人员
        dto.setUser(getCellStringValue(row.getCell(6)));            // 制单人
        dto.setData(getCellStringValue(row.getCell(7)));            // 备注信息

        // 解析结算详情信息（从第7列开始）
        ReceiptSettlementDetailDTO detail = new ReceiptSettlementDetailDTO();
        detail.setAccount(getCellStringValue(row.getCell(8)));      // 结算账户
        detail.setAmount(parseBigDecimal(getCellStringValue(row.getCell(9)))); // 结算金额
        detail.setSettle(getCellStringValue(row.getCell(10)));       // 结算号
        detail.setData(getCellStringValue(row.getCell(11)));        // 结算备注

        // 将结算详情添加到列表
        List<ReceiptSettlementDetailDTO> details = new ArrayList<>();
        details.add(detail);
        dto.setInfo(details);

        // 解析附件列
        String attachmentPaths = getCellStringValue(row.getCell(12));
        if (attachmentPaths != null && !attachmentPaths.trim().isEmpty()) {
            // 将逗号分隔的路径字符串转换为List
            List<String> fileList = Arrays.asList(attachmentPaths.split("\\s*,\\s*"));
            dto.setFile(fileList);
        } else {
            // 设置空列表作为默认值
            dto.setFile(new ArrayList<>());
        }

        return dto;
    }

    private LocalDateTime parseLocalDateTime(String timeStr) {
        try {
            if (timeStr != null && !timeStr.trim().isEmpty()) {
                return LocalDateTime.parse(timeStr);
            }
        } catch (Exception e) {
            log.warn("时间格式解析失败，使用当前时间: {}", timeStr);
        }
        return LocalDateTime.now();
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    @Override
    public byte[] exportSimpleReceiptReport() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            List<Receipt> receipts = receiptMapper.selectList(null);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("收款单简单报表");

            // 按照原型图中的表头顺序
            Row headerRow = sheet.createRow(0);
            String[] headers = {"所属组织", "客户", "单据时间", "单据编号", "单据金额", "关联人员", "制单人", "备注信息", "附件信息"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Receipt receipt : receipts) {
                Row row = sheet.createRow(rowNum++);
                // 按照表头顺序填充数据
                row.createCell(0).setCellValue(receipt.getFrame());        // 所属组织
                row.createCell(1).setCellValue(receipt.getCustomer());    // 客户
                row.createCell(2).setCellValue(receipt.getTime() != null ? receipt.getTime().toString() : ""); // 单据时间
                row.createCell(3).setCellValue(receipt.getNumber());        // 单据编号
                row.createCell(4).setCellValue(receipt.getTotal() != null ? receipt.getTotal().doubleValue() : 0); // 单据金额
                row.createCell(5).setCellValue(receipt.getPeople());        // 关联人员
                row.createCell(6).setCellValue(receipt.getUser());         // 制单人
                row.createCell(7).setCellValue(receipt.getData());          // 备注信息

                String attachmentInfo = "无附件";
                if (receipt.getFile() != null && !receipt.getFile().isEmpty()) {
                    attachmentInfo = String.join("; ", receipt.getFile());
                }
                row.createCell(8).setCellValue(attachmentInfo);            // 附件信息
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        } catch (Exception e) {
            log.error("生成简单报表失败", e);
            throw new RuntimeException("生成简单报表失败");
        }
    }

    @Override
    public byte[] exportDetailReceiptReport() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            List<Receipt> receipts = receiptMapper.selectList(null);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("收款单详细报表");

            // 详细报表表头（包含结算详情）
            Row headerRow = sheet.createRow(0);
            String[] headers = {"所属组织", "客户", "单据时间", "单据编号", "单据金额", "关联人员", "制单人",
                    "备注信息", "结算账户", "结算金额", "结算号", "结算备注", "附件信息"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Receipt receipt : receipts) {
                LambdaQueryWrapper<ReceiptDetail> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ReceiptDetail::getPid, receipt.getId());
                List<ReceiptDetail> details = receiptDetailMapper.selectList(wrapper);

                if (details.isEmpty()) {
                    Row row = sheet.createRow(rowNum++);
                    fillReceiptRow(receipt, row, null);
                } else {
                    for (ReceiptDetail detail : details) {
                        Row row = sheet.createRow(rowNum++);
                        fillReceiptRow(receipt, row, detail);
                    }
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        } catch (Exception e) {
            log.error("生成详细报表失败", e);
            throw new RuntimeException("生成详细报表失败");
        }
    }

    private void fillReceiptRow(Receipt receipt, Row row, ReceiptDetail detail) {
        // 按照表头顺序填充主表数据
        row.createCell(0).setCellValue(receipt.getFrame());        // 所属组织
        row.createCell(1).setCellValue(receipt.getCustomer());    // 客户
        row.createCell(2).setCellValue(receipt.getTime() != null ? receipt.getTime().toString() : ""); // 单据时间
        row.createCell(3).setCellValue(receipt.getNumber());        // 单据编号
        row.createCell(4).setCellValue(receipt.getTotal() != null ? receipt.getTotal().doubleValue() : 0); // 单据金额
        row.createCell(5).setCellValue(receipt.getPeople());        // 关联人员
        row.createCell(6).setCellValue(receipt.getUser());         // 制单人
        row.createCell(7).setCellValue(receipt.getData());          // 备注信息

        // 填充结算详情
        if (detail != null) {
            row.createCell(8).setCellValue(detail.getAccount());    // 结算账户
            row.createCell(9).setCellValue(detail.getMoney() != null ? detail.getMoney().doubleValue() : 0); // 结算金额
            row.createCell(10).setCellValue(detail.getSettle());   // 结算号
            row.createCell(11).setCellValue(detail.getData());      // 结算备注
        } else {
            row.createCell(8).setCellValue("");
            row.createCell(9).setCellValue(0);
            row.createCell(10).setCellValue("");
            row.createCell(11).setCellValue("");
        }

        // 附件信息
        String attachmentInfo = "无附件";
        if (receipt.getFile() != null && !receipt.getFile().isEmpty()) {
            attachmentInfo = String.join("; ", receipt.getFile());
        }
        row.createCell(12).setCellValue(attachmentInfo);           // 附件信息
    }

    private String getNucleusStatus(Integer nucleus) {
        if (nucleus == null) return "未知";
        switch (nucleus) {
            case 0: return "未核销";
            case 1: return "部分核销";
            case 2: return "已核销";
            default: return "未知";
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReceipt(ReceiptUpdateDTO receiptUpdateDTO) {
        // 1. 查询原收款单，检查审核状态
        Receipt oldReceipt = receiptMapper.selectById(receiptUpdateDTO.getId());
        if (oldReceipt != null && oldReceipt.getExamine() == 1) {
            throw new BusinessException("收款单已审核，不能直接修改，请先反审核");
        }

        // 2. 构建新的收款单实体
        Receipt receipt = new Receipt();
        receipt.setId(receiptUpdateDTO.getId());
        receipt.setCustomer(receiptUpdateDTO.getCustomer());
        receipt.setTime(receiptUpdateDTO.getTime());
        receipt.setNumber(receiptUpdateDTO.getNumber());
        receipt.setTotal(receiptUpdateDTO.getTotal());
        receipt.setPeople(receiptUpdateDTO.getPeople());
        receipt.setFile(receiptUpdateDTO.getFile());
        receipt.setData(receiptUpdateDTO.getData());
        receipt.setUser(receiptUpdateDTO.getUser());
        receipt.setFrame(receiptUpdateDTO.getFrame());

        try {
            if (receiptUpdateDTO.getInfo() != null) {
                String moreJson = objectMapper.writeValueAsString(receiptUpdateDTO.getInfo());
                receipt.setMore(moreJson);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 3. 更新主表数据
        receiptMapper.updateById(receipt);

        // 4. 删除原有明细
        receiptDetailMapper.deleteByPid(receiptUpdateDTO.getId());

        // 5. 插入新明细
        List<ReceiptSettlementDetailDTO> details = receiptUpdateDTO.getInfo();
        if (details != null && !details.isEmpty()) {
            for (ReceiptSettlementDetailDTO detail : details) {
                ReceiptDetail receiptDetail = new ReceiptDetail();
                receiptDetail.setAccount(detail.getAccount());
                receiptDetail.setMoney(detail.getAmount());
                receiptDetail.setSettle(detail.getSettle());
                receiptDetail.setData(detail.getData());
                receiptDetail.setId(String.valueOf(IdWorker.getId()));
                receiptDetail.setPid(receiptUpdateDTO.getId());

                receiptDetailMapper.insert(receiptDetail);
            }
        }
    }

    @Override
    public void auditReceipt(ReceiptAuditDTO receiptAuditDTO) {
        List<String> ids = receiptAuditDTO.getIds();

        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("审核单号不能为空");
        }

        for (String id : ids) {
            // 根据ID查询收款单
            Receipt receipt = receiptMapper.selectById(id);
            if (receipt == null) {
                throw new BusinessException("收款单不存在: " + id);
            }

            // 判断当前审核状态并进行切换
            if (receipt.getExamine() == 0) {
                // 未审核状态(0) -> 审核状态(1)
                receipt.setExamine(1);
            } else {
                // 已审核状态(1) -> 未审核状态(0)
                receipt.setExamine(0);
            }

            receiptMapper.updateById(receipt);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReceipt(ReceiptDeleteDTO receiptDeleteDTO) {
        List<String> receiptIds = receiptDeleteDTO.getReceiptIds();

        if (receiptIds == null || receiptIds.isEmpty()) {
            throw new BusinessException("收款单ID列表不能为空");
        }

        for (String id : receiptIds) {
            // 检查收款单是否存在
            Receipt receipt = receiptMapper.selectById(id);
            if (receipt == null) {
                throw new BusinessException("收款单不存在: " + id);
            }

            // 检查收款单是否已审核
            if (receipt.getExamine() == 1) {
                throw new BusinessException("收款单已审核，不能直接删除，请先反审核");
            }

            // 删除主表记录
            receiptMapper.deleteById(id);

            // 删除明细记录
            receiptDetailMapper.deleteByPid(id);
        }
    }
}