package com.zeroone.star.reportmanagement.entity;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 在导出销售订单跟踪表时进行类型转换
 *
 * @author leyu
 * @date 2025-11-14
 */
public class SalesOrderStatusConverter implements Converter<Integer> {
    @Override
    public Class<?> supportJavaTypeKey() {
        // 转换 Java 的 Integer 类型
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        // 写入 Excel 的 String 类型
        return CellDataTypeEnum.STRING;
    }

    /**
     * 写入Excel时调用 (Java Integer -> Excel String)
     */
    @Override
    public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {

        String text = "";
        if (value != null) {
            switch (value) {
                case 0:
                    text = "未入库";
                    break;
                case 1:
                    text = "部分入库";
                    break;
                case 2:
                    text = "已入库";
                    break;
                case 3:
                    text = "关闭";
                    break;
                default:
                    text = "未知状态";
            }
        }
        return new WriteCellData<>(text);
    }
}
