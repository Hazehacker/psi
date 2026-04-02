package com.zeroone.star.purchase.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class StatusToStringConverter implements Converter<Integer> {

    @Override
    public Class<Integer> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public WriteCellData<String> convertToExcelData(
            Integer value,
            ExcelContentProperty contentProperty,
            GlobalConfiguration globalConfiguration) {

        if (value == null) {
            return new WriteCellData<>("");
        }

        // 获取字段名（不是表头名）
        String fieldName = contentProperty != null && contentProperty.getField() != null
                ? contentProperty.getField().getName()
                : "";

        String text;
        switch (fieldName) {
            case "examine":
                text = (value == 1 ? "已审核" : "未审核");
                break;

            case "nucleus":
                switch (value) {
                    case 0: text = "未核销"; break;
                    case 1: text = "部分核销"; break;
                    case 2: text = "已核销"; break;
                    default: text = ""; break;
                }
                break;

            case "cse":
                switch (value) {
                    case 0: text = "未结算"; break;
                    case 1: text = "部分结算"; break;
                    case 2: text = "已结算"; break;
                    case 3: text = "无需结算"; break;
                    default: text = ""; break;
                }
                break;

            case "invoice":
                switch (value) {
                    case 0: text = "未开票"; break;
                    case 1: text = "部分开票"; break;
                    case 2: text = "已开票"; break;
                    case 3: text = "无需开具"; break;
                    default: text = ""; break;
                }
                break;

            case "check":
                text = (value == 1 ? "已核对" : "未核对");
                break;

            default:
                text = (value == 1 ? "是" : "否");
                break;
        }

        return new WriteCellData<>(text);
    }
}
