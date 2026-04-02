package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesOrderTrackingFormDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesReceiptDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesReceiptQuery;
import com.zeroone.star.reportmanagement.mapper.salesreport.SalesReceiptMapper;
import com.zeroone.star.reportmanagement.service.SalesReceiptService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 销售收款表服务实现
 *
 * @author leyu
 * @date 2025-10-26
 */
@Service
public class SalesReceiptServiceImpl implements SalesReceiptService {

    @Autowired
    private SalesReceiptMapper mapper;

    @Autowired
    private EasyExcelComponent excel;

    /**
     * 带条件的分页查询
     * @param query 查询条件
     * @return PageDTO
     */
    @Override
    public PageDTO<SalesReceiptDTO> listAllPage(SalesReceiptQuery query) {
        Page<SalesReceiptDTO> page = new Page<>(query.getPageIndex(),  query.getPageSize());
        Page<SalesReceiptDTO> result = mapper.selectSalesReceipt(page, query);
        return PageDTO.create(result);
    }

    /**
     * 将所有查询内容输出为excel的byte array
     * @param query 查询条件
     * @return excel数据的byte array
     */
    @SneakyThrows
    @Override
    public byte[] exportExcel(SalesReceiptQuery query) {
        List<SalesReceiptDTO> list = mapper.selectAllWithCondition(query);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        excel.export("销售收款表", out, SalesReceiptDTO.class, list);

        byte[] excel = out.toByteArray();
        out.close();
        return excel;
    }
}
