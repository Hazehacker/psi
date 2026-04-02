package com.zeroone.star.reportmanagement.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderSummaryFormDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderSummaryFormExportDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderSummaryFormQuery;
import com.zeroone.star.reportmanagement.mapper.procurementreport.PurchaseOrderSummaryFormMapper;
import com.zeroone.star.reportmanagement.service.PurchaseOrderSummaryFormService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PurchaseOrderSummaryFormServiceImpl extends ServiceImpl<PurchaseOrderSummaryFormMapper,PurchaseOrderSummaryFormDTO> implements PurchaseOrderSummaryFormService {


    @Override
    public PageDTO<PurchaseOrderSummaryFormDTO> listPurchaseSummary(PurchaseOrderSummaryFormQuery query) {
        Long pageIndex = query.getPageIndex();
        Long pageSize = query.getPageSize();
        Page<PurchaseOrderSummaryFormDTO> page;
        if(pageIndex > 0 && pageSize > 0){
            page = new Page<>(query.getPageIndex(), query.getPageSize());
        }else{
            // 查所有：MyBatis-Plus 支持 size = -1 表示不限制
            page = new Page<>( 1, -1);
        }
        Page<PurchaseOrderSummaryFormDTO> pageRes = baseMapper.listPurchaseSummary(page, query);
        return PageDTO.create(pageRes);
    }

    public ResponseEntity<byte[]> export(PurchaseOrderSummaryFormQuery query) throws IOException {
        PageDTO<PurchaseOrderSummaryFormDTO> pageDTO = listPurchaseSummary(query);
        List<PurchaseOrderSummaryFormDTO> tempList = pageDTO.getRows();
        List<PurchaseOrderSummaryFormExportDTO> rows = new ArrayList<>();
        for (PurchaseOrderSummaryFormDTO dto : tempList) {
            PurchaseOrderSummaryFormExportDTO item = new PurchaseOrderSummaryFormExportDTO(
                    dto.getSupplier(),dto.getUser(),dto.getPeople(),
                    dto.getGoods(),dto.getAttr(),dto.getWarehouse(),dto.getUnit(),
                    dto.getBuy().getPrice(),dto.getBuy().getNums(),dto.getBuy().getMoney(),
                    dto.getBor().getPrice(),dto.getBor().getNums(),dto.getBor().getMoney(),
                    dto.getSummary().getNums(),dto.getSummary().getMoney());
            rows.add(item);
        }
        if(CollUtil.isEmpty(rows)){
            List<PurchaseOrderSummaryFormExportDTO> temp = new ArrayList<>();
            temp.add(new PurchaseOrderSummaryFormExportDTO());
            //避免导出空值,导致的excel损坏
            rows = temp;
        }
        // 定义输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 根据query.getSummaryType()汇总类型去屏蔽对应的属性 供应商supplier，用户user，关联人员people
        Set<String> excludeFiled = new HashSet<>();
        excludeFiled.add("supplier");
        excludeFiled.add("user");
        excludeFiled.add("people");
        String summaryType = query.getSummaryType();
        if(summaryType != null && summaryType.matches("^(user|supplier|people)$")){
            excludeFiled.remove(summaryType);
        }


        // 因为导出Excel的第一列是动态的, 所以重写export方法
        Class clazz = PurchaseOrderSummaryFormExportDTO.class;
        int MAX_COUNT_PER_SHEET = 5000;
        ExcelWriterBuilder builder = EasyExcel.write(out, clazz);
        ExcelWriter writer = builder.excludeColumnFiledNames(excludeFiled).build();
        //计算总页数
        int sheetCount = rows.size() / MAX_COUNT_PER_SHEET;
        sheetCount = rows.size() % MAX_COUNT_PER_SHEET == 0 ? sheetCount : sheetCount + 1;
        //循环构建分页
        for (int i = 0; i < sheetCount; i++) {
            //创建一个页签
            WriteSheet sheet = new WriteSheet();
            sheet.setSheetNo(i);
            sheet.setSheetName("采购汇总表" + (i + 1));
            //设置数据起始位置
            int start = i * MAX_COUNT_PER_SHEET;
            int end = (i + 1) * MAX_COUNT_PER_SHEET;
            end = Math.min(end, rows.size());
            //写入数据到页签
            writer.write(rows.subList(start, end), sheet);
        }
        writer.finish();
        out.close();

        // 响应给前端
        HttpHeaders headers = new HttpHeaders();
        String filename = "rep-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
    }
}
