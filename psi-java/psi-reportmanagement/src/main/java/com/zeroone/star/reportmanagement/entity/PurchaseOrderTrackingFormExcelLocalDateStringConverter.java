package com.zeroone.star.reportmanagement.entity;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author yu
 * @date 2025/10/25
 * EasyExcel LocalDate 转换器
 * (将LocalDate转为yyyy-MM-dd格式的字符串)
 */
public class PurchaseOrderTrackingFormExcelLocalDateStringConverter implements Converter<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Class<?> supportJavaTypeKey() {
        // 转换Java的LocalDate类型
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        // 写入Excel的String类型
        return CellDataTypeEnum.STRING;
    }

    /**
     * 写入Excel时调用(Java->Excel)
     */
    @Override
    public WriteCellData<String> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (value == null) {
            return new WriteCellData<>("");
            // Java null -> Excel 空字符串
        }
        // 将 LocalDate 格式化为 "yyyy-MM-dd" 字符串
        return new WriteCellData<>(value.format(FORMATTER));
    }

}
