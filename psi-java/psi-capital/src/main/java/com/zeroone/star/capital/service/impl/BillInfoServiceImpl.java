package com.zeroone.star.capital.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zeroone.star.capital.DO.BillDO;
import com.zeroone.star.capital.DO.BillInfoDO;
import com.zeroone.star.capital.DO.ImyBillDO;
import com.zeroone.star.capital.DO.OmyBillDO;
import com.zeroone.star.capital.DO.OmyDO;
import com.zeroone.star.capital.mapper.BillInfoMapper;
import com.zeroone.star.capital.mapper.BillMapper;
import com.zeroone.star.capital.mapper.BuyMapper;
import com.zeroone.star.capital.mapper.ImyBillMapper;
import com.zeroone.star.capital.mapper.OmyBillMapper;
import com.zeroone.star.capital.mapper.OmyMapper;
import com.zeroone.star.capital.service.IBillInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.capital.DO.*;
import com.zeroone.star.capital.mapper.BreBillMapper;
import com.zeroone.star.capital.mapper.BreMapper;
import com.zeroone.star.capital.mapper.ImyMapper;
import com.zeroone.star.capital.mapper.SellBillMapper;
import com.zeroone.star.capital.mapper.SellMapper;
import com.zeroone.star.capital.mapper.SreBillMapper;
import com.zeroone.star.capital.mapper.SreMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 核销单详情 服务实现类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Slf4j
@Service
public class BillInfoServiceImpl extends ServiceImpl<BillInfoMapper, BillInfoDO> implements IBillInfoService {

    @Resource
    EasyExcelComponent excel;

    @Resource
    BillInfoMapper billInfoMapper;

    @Resource
    BillMapper billMapper;

    @Resource
    BreMapper breMapper;

    @Resource
    BuyMapper buyMapper;

    @Resource
    ImyMapper imyMapper;

    @Resource
    OmyMapper omyMapper;

    @Autowired
    @Qualifier("sellMapper")
    SellMapper sellMapper;

    @Resource
    SreMapper sreMapper;

    @Resource
    SellBillMapper sellBillMapper;

    @Resource
    SreBillMapper sreBillMapper;

    @Resource
    BreBillMapper breBillMapper;

    @Resource
    ImyBillMapper imyBillMapper;

    @Resource
    OmyBillMapper omyBillMapper;

    /**
     * 将核销类型整数转换为字符串
     */
    private String getVerificationTypeString(Integer type) {
        if (type == null) {
            return "未知";
        }
        switch (type) {
            case 0: return "预收冲应收";
            case 1: return "预付冲应付";
            case 2: return "应收冲应付";
            case 3: return "销退冲销售";
            case 4: return "购退冲采购";
            default: return "未知";
        }
    }

    /**
     * 计算已核销金额
     * @param documentType 单据类型
     * @param documentId 单据ID
     * @return 已核销金额
     */
    private BigDecimal calculateVerifiedAmount(String documentType, String documentId) {
        if (documentId == null || documentId.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal verifiedAmount = BigDecimal.ZERO;

        try {
            switch (documentType) {
                case "销售单":
                    List<SellBillDO> sellBills = sellBillMapper.selectList(
                            new LambdaQueryWrapper<SellBillDO>().eq(SellBillDO::getPid, documentId)
                    );
                    verifiedAmount = sellBills.stream()
                            .map(SellBillDO::getMoney)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    break;

                case "销售退货单":
                    List<SreBillDO> sreBills = sreBillMapper.selectList(
                            new LambdaQueryWrapper<SreBillDO>().eq(SreBillDO::getPid, documentId)
                    );
                    verifiedAmount = sreBills.stream()
                            .map(SreBillDO::getMoney)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    break;

                case "采购退货单":
                    List<BreBillDO> breBills = breBillMapper.selectList(
                            new LambdaQueryWrapper<BreBillDO>().eq(BreBillDO::getPid, documentId)
                    );
                    verifiedAmount = breBills.stream()
                            .map(BreBillDO::getMoney)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    break;

                case "收款单":
                    List<ImyBillDO> imyBills = imyBillMapper.selectList(
                            new LambdaQueryWrapper<ImyBillDO>().eq(ImyBillDO::getPid, documentId)
                    );
                    verifiedAmount = imyBills.stream()
                            .map(ImyBillDO::getMoney)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    break;

                case "付款单":
                    List<OmyBillDO> omyBills = omyBillMapper.selectList(
                            new LambdaQueryWrapper<OmyBillDO>().eq(OmyBillDO::getPid, documentId)
                    );
                    verifiedAmount = omyBills.stream()
                            .map(OmyBillDO::getMoney)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    break;

                case "采购单":
                    // 采购单通过bill_info表汇总计算
                    List<BillInfoDO> buyBillInfos = billInfoMapper.selectList(
                            new LambdaQueryWrapper<BillInfoDO>().eq(BillInfoDO::getSource, documentId)
                    );
                    verifiedAmount = buyBillInfos.stream()
                            .map(BillInfoDO::getMoney)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    break;

                default:
                    verifiedAmount = BigDecimal.ZERO;
                    break;
            }
        } catch (Exception e) {
            log.error("计算已核销金额失败，单据类型: {}, 单据ID: {}", documentType, documentId, e);
            verifiedAmount = BigDecimal.ZERO;
        }

        return verifiedAmount;
    }

    /**
     * 导出详细报表
     * @param ids 核销单ID列表
     * @return 文件流
     */
    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> exportBillInfo(List<String> ids) {
        // 1. 查询核销单主表数据
        List<BillDO> billDOS = billMapper.selectBatchIds(ids);
        if (billDOS == null || billDOS.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("未找到对应的核销单数据".getBytes());
        }

        // 2. 查询核销单详情数据
        List<BillInfoDO> billInfoDOS = billInfoMapper.selectList(
                new LambdaQueryWrapper<BillInfoDO>().in(BillInfoDO::getPid, ids)
        );

        // 3. 按核销单ID分组详情数据
        Map<String, List<BillInfoDO>> grouped = billInfoDOS.stream()
                .collect(Collectors.groupingBy(BillInfoDO::getPid));

        // 4. 定义ZIP输出流
        ByteArrayOutputStream zipOutStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(zipOutStream);

        // 5. 遍历每个核销单
        for (BillDO billDO : billDOS) {
            String billId = billDO.getId();
            List<BillInfoDO> billGroup = grouped.getOrDefault(billId, new ArrayList<>());

            // === Excel内存输出流 ===
            ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
            ExcelWriter writer = EasyExcel.write(excelOut).build();
            WriteSheet sheet = EasyExcel.writerSheet("核销单-" + billId).build();

            // ================== 一、主表信息行 ==================
            List<List<String>> mainInfoRow = new ArrayList<>();
            String customer = billDO.getCustomer() != null ? billDO.getCustomer() : "";
            String supplier = billDO.getSupplier() != null ? billDO.getSupplier() : "";
            String billDate = billDO.getTime() != null 
                    ? new SimpleDateFormat("yyyy-MM-dd").format(billDO.getTime()) 
                    : LocalDate.now().toString();
            String billNumber = billDO.getNumber() != null ? billDO.getNumber() : "";
            
            mainInfoRow.add(Arrays.asList(
                    "客户:" + customer,
                    "供应商:" + supplier,
                    "单据日期:" + billDate,
                    "单据编号:" + billNumber
            ));
            writer.write(mainInfoRow, sheet);

            // 空行
            writer.write(Collections.singletonList(Collections.singletonList("")), sheet);

            // ================== 二、表头 ==================
            List<List<String>> header = new ArrayList<>();
            header.add(Arrays.asList("核销类型", "单据类型", "单据日期", "单据编号", "单据金额", "已核销", "未核销", "核销金额"));
            writer.write(header, sheet);

            // ================== 三、明细行 ==================
            List<List<String>> rows = new ArrayList<>();

            for (BillInfoDO detail : billGroup) {
                String hxType = detail.getBill();   // 核销类型，比如"预收冲应收"
                String docType = detail.getMold();  // 单据类型，从bill_info.mold获取
                String sourceId = detail.getSource(); // 关联单据ID
                BigDecimal amount = detail.getMoney() != null ? detail.getMoney() : BigDecimal.ZERO;

                // ====== 1. 根据单据类型查询对应单据表（通过ID查询） ======
                LocalDate docDate = null;
                BigDecimal docMoney = BigDecimal.ZERO;
                String docNumber = "";

                try {
                    switch (docType) {
                        case "采购单":
                            BuyDO buyDO = buyMapper.selectById(sourceId);
                            if (buyDO != null) {
                                docDate = buyDO.getTime() != null ? buyDO.getTime().toLocalDate() : LocalDate.now();
                                docMoney = buyDO.getTotal() != null ? buyDO.getTotal() : BigDecimal.ZERO;
                                docNumber = buyDO.getNumber() != null ? buyDO.getNumber() : "";
                            }
                            break;

                        case "采购退货单":
                            BreDO breDO = breMapper.selectById(sourceId);
                            if (breDO != null) {
                                docDate = breDO.getTime() != null ? LocalDate.parse(breDO.getTime()) : LocalDate.now();
                                docMoney = breDO.getTotal() != null ? breDO.getTotal() : BigDecimal.ZERO;
                                docNumber = breDO.getNumber() != null ? breDO.getNumber() : "";
                            }
                            break;

                        case "付款单":
                            OmyDO omyDO = omyMapper.selectById(sourceId);
                            if (omyDO != null) {
                                if (omyDO.getTime() != null) {
                                    docDate = omyDO.getTime()
                                            .atZone(java.time.ZoneId.systemDefault())
                                            .toLocalDate();
                                } else {
                                    docDate = LocalDate.now();
                                }
                                docMoney = omyDO.getTotal() != null ? omyDO.getTotal() : BigDecimal.ZERO;
                                docNumber = omyDO.getNumber() != null ? omyDO.getNumber() : "";
                            }
                            break;

                        case "收款单":
                            ImyDO imyDO = imyMapper.selectById(sourceId);
                            if (imyDO != null) {
                                docDate = imyDO.getTime() != null 
                                        ? imyDO.getTime().toLocalDate() 
                                        : LocalDate.now();
                                docMoney = imyDO.getTotal() != null ? imyDO.getTotal() : BigDecimal.ZERO;
                                docNumber = imyDO.getNumber() != null ? imyDO.getNumber() : "";
                            } else {
                                log.warn("收款单查询失败，单据ID: {}", sourceId);
                            }
                            break;

                        case "销售单":
                            log.info("开始查询销售单，单据ID: {}, Mapper类型: {}, Mapper类名: {}", 
                                    sourceId, sellMapper.getClass().getSimpleName(), sellMapper.getClass().getName());
                            SellDO sellDO = sellMapper.selectById(sourceId);
                            log.info("销售单查询结果: {}, 查询到的数据: {}", sellDO != null ? "找到数据" : "未找到数据", 
                                    sellDO != null ? sellDO.toString() : "null");
                            if (sellDO != null) {
                                if (sellDO.getTime() != null) {
                                    docDate = sellDO.getTime().toLocalDate();
                                } else {
                                    log.warn("销售单日期字段为null，单据ID: {}", sourceId);
                                    docDate = LocalDate.now();
                                }
                                docMoney = sellDO.getTotal() != null ? sellDO.getTotal() : BigDecimal.ZERO;
                                docNumber = sellDO.getNumber() != null ? sellDO.getNumber() : "";
                                log.debug("销售单查询成功，单据ID: {}, 单据编号: {}, 单据金额: {}", sourceId, docNumber, docMoney);
                            } else {
                                log.warn("销售单查询失败，单据ID: {}", sourceId);
                            }
                            break;

                        case "销售退货单":
                            SreDO sreDO = sreMapper.selectById(sourceId);
                            if (sreDO != null) {
                                docDate = sreDO.getTime() != null 
                                        ? sreDO.getTime().toLocalDate() 
                                        : LocalDate.now();
                                docMoney = sreDO.getTotal() != null ? sreDO.getTotal() : BigDecimal.ZERO;
                                docNumber = sreDO.getNumber() != null ? sreDO.getNumber() : "";
                            }
                            break;

                        default:
                            docDate = LocalDate.now();
                            docMoney = BigDecimal.ZERO;
                            docNumber = "";
                            break;
                    }
                } catch (Exception e) {
                    log.error("查询单据信息失败，单据类型: {}, 单据ID: {}", docType, sourceId, e);
                    docDate = LocalDate.now();
                    docMoney = BigDecimal.ZERO;
                    docNumber = "";
                }

                // ====== 2. 计算已核销金额和未核销金额 ======
                BigDecimal verifiedAmount = calculateVerifiedAmount(docType, sourceId);
                BigDecimal unverifiedAmount = docMoney.subtract(verifiedAmount);
                if (unverifiedAmount.compareTo(BigDecimal.ZERO) < 0) {
                    unverifiedAmount = BigDecimal.ZERO;
                }

                // ====== 3. 组装 Excel 明细行 ======
                rows.add(Arrays.asList(
                        hxType != null ? hxType : "",                    // 核销类型
                        docType != null ? docType : "",                 // 单据类型
                        docDate != null ? docDate.toString() : "",      // 单据日期
                        docNumber,                                      // 单据编号
                        docMoney.toPlainString(),                       // 单据金额
                        verifiedAmount.toPlainString(),                 // 已核销
                        unverifiedAmount.toPlainString(),               // 未核销
                        amount.toPlainString()                          // 核销金额
                ));

            }

            writer.write(rows, sheet);


            // ================== 四、汇总信息 ==================
            // 从bill表获取汇总信息
            String hxType = getVerificationTypeString(billDO.getType());
            
            // 从bill_info汇总核销金额
            BigDecimal totalHxMoney = billGroup.stream()
                    .map(BillInfoDO::getMoney)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            String relatedPerson = billDO.getPeople() != null ? billDO.getPeople() : "";
            String remark = billDO.getData() != null ? billDO.getData() : "无";

            writer.write(Collections.singletonList(Collections.singletonList("")), sheet); // 空行
            List<List<String>> summary = new ArrayList<>();
            summary.add(Arrays.asList(
                    "核销类型:" + hxType,
                    "核销金额:" + totalHxMoney.toPlainString(),
                    "关联人员:" + relatedPerson,
                    "备注信息:" + remark
            ));
            writer.write(summary, sheet);

            writer.finish();

            // ================== 写入ZIP ==================
            String fileName = "核销单-" + billId + ".xlsx";
            ZipEntry entry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write(excelOut.toByteArray());
            zipOutputStream.closeEntry();

            excelOut.close();
        }

        zipOutputStream.close();
        byte[] bytes = zipOutStream.toByteArray();
        
        // 生成唯一文件名（时间戳）
        String filename = "核销单详情报表-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + ".zip";

        // 3.处理中文文件名兼容性
        // 指定处理方式
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build();

        // 响应头
        HttpHeaders headers = new HttpHeaders();

        headers.setContentDisposition(contentDisposition);

        // 设置响应类型为zip
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.setContentLength(bytes.length);

        // 指定编码
        headers.set("Content-Encoding", "UTF-8");

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
