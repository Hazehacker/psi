package com.zeroone.star.reportmanagement.config;

import com.alibaba.excel.EasyExcel;
import com.zeroone.star.project.dto.j5.salesreport.SalesSummaryExportDTO;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DynamicExcelExporter {

    /**
     * 动态导出Excel
     */
    public void export(String sheetName, OutputStream out,
                       List<SalesSummaryExportDTO> data, String groupBy) throws Exception {

        // 构建动态表头
        List<List<String>> head = buildDynamicHead(groupBy);

        // 构建动态数据
        List<List<Object>> dataList = buildDynamicData(data, groupBy);

        // 使用EasyExcel写入
        EasyExcel.write(out)
                .head(head)
                .sheet(sheetName)
                .doWrite(dataList);
    }

    /**
     * 构建动态表头
     */
    private List<List<String>> buildDynamicHead(String groupBy) {
        List<List<String>> head = new ArrayList<>();

        // 根据分组条件决定是否添加groupField列
        if (!"product".equals(groupBy)) {
            String groupFieldName = getGroupFieldName(groupBy);
            head.add(Collections.singletonList(groupFieldName));
        }

        // 固定列
        head.add(Collections.singletonList("商品名称"));
        head.add(Collections.singletonList("属性"));
        head.add(Collections.singletonList("仓库"));
        head.add(Collections.singletonList("单位"));
        head.add(Collections.singletonList("销售单价"));
        head.add(Collections.singletonList("销售数量"));
        head.add(Collections.singletonList("销售金额"));
        head.add(Collections.singletonList("购退单价"));
        head.add(Collections.singletonList("购退数量"));
        head.add(Collections.singletonList("购退金额"));
        head.add(Collections.singletonList("汇总数量"));
        head.add(Collections.singletonList("汇总金额"));

        return head;
    }

    /**
     * 构建动态数据
     */
    private List<List<Object>> buildDynamicData(List<SalesSummaryExportDTO> data, String groupBy) {
        List<List<Object>> dataList = new ArrayList<>();

        for (SalesSummaryExportDTO dto : data) {
            List<Object> row = new ArrayList<>();

            // 根据分组条件决定是否添加groupField数据
            if (!"product".equals(groupBy)) {
                row.add(dto.getGroupField() != null ? dto.getGroupField() : "");
            }

            // 固定数据
            row.add(dto.getProductName());
            row.add(dto.getAttribute());
            row.add(dto.getWarehouse());
            row.add(dto.getUnit());
            row.add(dto.getSalesPrice());
            row.add(dto.getSalesQuantity());
            row.add(dto.getSalesAmount());
            row.add(dto.getReturnPrice());
            row.add(dto.getReturnQuantity());
            row.add(dto.getReturnAmount());
            row.add(dto.getTotalQuantity());
            row.add(dto.getTotalAmount());

            dataList.add(row);
        }

        return dataList;
    }

    /**
     * 根据分组类型获取groupField的显示名称
     */
    private String getGroupFieldName(String groupBy) {
        switch (groupBy) {
            case "customer":
                return "客户";
            case "user":
                return "用户";
            case "people":
                return "关联人员";
            default:
                return "分组";
        }
    }
}