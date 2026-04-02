package com.zeroone.star.project.components.easyexcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements Converter<LocalDate> {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDate.class;
    }
    
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }
    
    @Override
    public LocalDate convertToJavaData(ReadConverterContext<?> context) {
        return LocalDate.parse(context.getReadCellData().getStringValue(), formatter);
    }
    
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<LocalDate> context) {
        return new WriteCellData<>(context.getValue().format(formatter));
    }
}