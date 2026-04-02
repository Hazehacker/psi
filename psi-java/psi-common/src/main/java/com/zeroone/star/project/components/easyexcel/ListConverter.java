package com.zeroone.star.project.components.easyexcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 类型转换
 * autor:fy
 */
public class ListConverter implements Converter<List<?>> {

    // 定义 List 元素的分隔符（例如逗号）
    private static final String SEPARATOR = ",";

    @Override
    public Class<?> supportJavaTypeKey() {
        // 支持的 Java 类型：List
        return List.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        // Excel 中对应的类型：字符串
        return CellDataTypeEnum.STRING;
    }

    /**
     * Excel 读操作：将单元格的字符串转为 List
     */
    @Override
    public List<?> convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String value = cellData.getStringValue();
        if (value == null || value.isEmpty()) {
            return new ArrayList<>();
        }
        // 按分隔符拆分字符串为 List
        List<String> list = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(value, SEPARATOR);
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken().trim());
        }
        return list;
    }

    /**
     * Excel 写操作：将 List 转为单元格的字符串
     */
    @Override
    public WriteCellData<?> convertToExcelData(List<?> value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (value == null || value.isEmpty()) {
            return new WriteCellData<>("");
        }
        // 将 List 元素用分隔符拼接为字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.size(); i++) {
            sb.append(value.get(i).toString());
            if (i != value.size() - 1) {
                sb.append(SEPARATOR);
            }
        }
        return new WriteCellData<>(sb.toString());
    }
}