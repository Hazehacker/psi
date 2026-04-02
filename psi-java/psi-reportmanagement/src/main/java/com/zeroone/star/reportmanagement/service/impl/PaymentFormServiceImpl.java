package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PaymentFormExcelDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PaymentFormFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PaymentFormQuery;
import com.zeroone.star.reportmanagement.mapper.procurementreport.PurchasePaymentReportMapper;
import com.zeroone.star.reportmanagement.service.PaymentFormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentFormServiceImpl implements PaymentFormService {
    private final PurchasePaymentReportMapper reportMapper;
    private final EasyExcelComponent easyExcelComponent;

    @Override
    public PageDTO<PaymentFormFormDTO> listPaymentForm(PaymentFormQuery query) {
        // 创建分页对象
        Page<PaymentFormFormDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());

        // 查询数据
        page = reportMapper.selectPurchasePaymentList(page, query);

        // 构建分页结果
        return PageDTO.create(page);
    }

    @Override
    public byte[] exportPaymentFormToExcel(PaymentFormQuery query) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<PaymentFormExcelDTO> exportData = new ArrayList<>();

            // 检查分页参数是否合法
            if (query.getPageIndex() > 0 && query.getPageSize() > 0) {
                // 分页参数合法, 按照分页导出数据
                Page<PaymentFormFormDTO> pageResult = reportMapper.selectPurchasePaymentList(
                        new Page<>(query.getPageIndex(), query.getPageSize()), query);

                // 转换当前页数据
                for (PaymentFormFormDTO dto : pageResult.getRecords()) {
                    PaymentFormExcelDTO excelDTO = convertToExcelDTO(dto);
                    exportData.add(excelDTO);
                }
            } else {
                // 分页参数不合法，查出全部数据并导出
                int pageSize = 5000;
                int pageNum = 1;

                // 循环分页查询所有数据
                boolean hasMore = true;
                while (hasMore) {
                    Page<PaymentFormFormDTO> resultPage = reportMapper.selectPurchasePaymentList(
                            new Page<>(pageNum, pageSize), query
                    );
                    List<PaymentFormFormDTO> records = resultPage.getRecords();
                    // 转换当前页数据
                    for (PaymentFormFormDTO dto : records) {
                        PaymentFormExcelDTO excelDTO = convertToExcelDTO(dto);
                        exportData.add(excelDTO);
                    }
                    // 如果当前页数小于总页数, 说明还有更多数据
                    hasMore = resultPage.getCurrent() < resultPage.getPages();
                    pageNum++;
                }
            }
            // 导出数据到Excel
            easyExcelComponent.export("采购付款表", outputStream, PaymentFormExcelDTO.class, exportData);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("导出采购付款表Excel失败", e);
            throw new RuntimeException("导出Excel失败");
        }
    }

    /**
     * 将PaymentFormFormDTO转换为PaymentFormExcelDTO
     */
    private PaymentFormExcelDTO convertToExcelDTO(PaymentFormFormDTO dto) {
        PaymentFormExcelDTO excelDTO = new PaymentFormExcelDTO();
        excelDTO.setType(dto.getType());
        excelDTO.setFrame(dto.getFrame());
        excelDTO.setSupplier(dto.getSupplier());
        excelDTO.setTime(dto.getTime());
        excelDTO.setNumber(dto.getNumber());
        excelDTO.setTotal(dto.getTotal());
        excelDTO.setActual(dto.getActual());
        excelDTO.setPayment(dto.getPayment());
        excelDTO.setBalance(dto.getBalance());
        excelDTO.setRate(dto.getRate());
        excelDTO.setNucleus(dto.getNucleus()); // 会自动转换文字
        excelDTO.setData(dto.getData());
        return excelDTO;
    }
}
