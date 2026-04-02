package com.zeroone.star.sale.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

// 映射 Excel 单行数据（主表字段0-8 + 明细字段9-21）
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("exel数据映射")
public class SaleOrderExcelRow {
    // ------------------- 主表字段（Excel第1-9列，index=0-8） -------------------
    @ExcelProperty(index = 0) // Excel第1列：客户
    private String customer;

    // 指定自定义日期转换器（同时支持导入和导出）
    @ExcelProperty(index = 1, converter = CustomDateConverter.class)
    private LocalDate orderDate;

    @ExcelProperty(index = 2) // Excel第3列：单据编号（区分不同订单的核心）
    private String orderNumber;

    @ExcelProperty(index = 3) // Excel第4列：单据金额
    private BigDecimal totalAmount;

    @ExcelProperty(index = 4) // Excel第5列：实际金额
    private BigDecimal actualAmount;

    @ExcelProperty(index = 5) // Excel第6列：关联人员
    private String relatedPerson;

    @ExcelProperty(index = 6, converter = CustomDateConverter.class)
    private LocalDate arrivalDate;

    @ExcelProperty(index = 7) // Excel第8列：物流信息
    private String logisticsInfo;

    @ExcelProperty(index = 8) // Excel第9列：订单备注
    private String orderRemark;

    // ------------------- 明细字段（Excel第10-22列，index=9-21） -------------------
    @ExcelProperty(index = 9)  // Excel第10列：商品名称
    private String productName;

    @ExcelProperty(index = 10) // Excel第11列：辅助属性
    private String auxiliaryAttribute;

    @ExcelProperty(index = 11) // Excel第12列：单位
    private String unit;

    @ExcelProperty(index = 12) // Excel第13列：仓库
    private String warehouse;

    @ExcelProperty(index = 13) // Excel第14列：单价
    private BigDecimal unitPrice;

    @ExcelProperty(index = 14) // Excel第15列：数量
    private BigDecimal quantity;

    @ExcelProperty(index = 15) // Excel第16列：折扣率(%)
    private BigDecimal discountRate;

    @ExcelProperty(index = 16) // Excel第17列：折扣额
    private BigDecimal discountAmount;

    @ExcelProperty(index = 17) // Excel第18列：金额（不含税）
    private BigDecimal amount;

    @ExcelProperty(index = 18) // Excel第19列：税率(%)
    private BigDecimal taxRate;

    @ExcelProperty(index = 19) // Excel第20列：税额
    private BigDecimal taxAmount;

    @ExcelProperty(index = 20) // Excel第21列：价税合计
    private BigDecimal totalWithTax;

    @ExcelProperty(index = 21) // Excel第22列：商品备注
    private String productRemark;


    /**
     * 自定义日期转换器：同时支持
     * 1. 导入（Excel字符串 → Java LocalDate）
     * 2. 导出（Java LocalDate → Excel字符串）
     */
    public static class CustomDateConverter implements Converter<LocalDate> {
        // 支持的导入日期格式（根据Excel实际数据补充）
        private static final String[] IMPORT_FORMATS = {
                "yyyy-MM-dd", "yyyy/MM/dd", "MM/dd/yyyy",
                "yyyy.MM.dd", "yyyy年MM月dd日"
        };
        // 导出时的目标格式
        private static final String EXPORT_FORMAT = "yyyy-MM-dd";


        // 导入：将Excel中的字符串转换为LocalDate
        @Override
        public LocalDate convertToJavaData(
                ReadCellData<?> cellData,
                ExcelContentProperty contentProperty,
                GlobalConfiguration globalConfiguration) {
            String dateStr = cellData.getStringValue().trim();
            if (dateStr.isEmpty()) {
                return null;
            }

            // 尝试所有支持的导入格式
            for (String format : IMPORT_FORMATS) {
                try {
                    return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(format));
                } catch (DateTimeParseException e) {
                    continue;
                }
            }

            // 所有格式不匹配时抛出异常，提示具体错误
            throw new IllegalArgumentException(
                    "导入日期格式错误！值：" + dateStr + "，支持格式：" + String.join("、", IMPORT_FORMATS)
            );
        }


        // 导出：将LocalDate转换为指定格式的字符串
        @Override
        public WriteCellData<?> convertToExcelData(
                LocalDate value,
                ExcelContentProperty contentProperty,
                GlobalConfiguration globalConfiguration) {
            if (value == null) {
                return new WriteCellData<>(""); // 空值返回空字符串
            }
            // 按导出格式转换
            String formattedDate = value.format(DateTimeFormatter.ofPattern(EXPORT_FORMAT));
            return new WriteCellData<>(formattedDate);
        }


        // 声明支持的Java类型（必须是LocalDate，与字段类型一致）
        @Override
        public Class<LocalDate> supportJavaTypeKey() {
            return LocalDate.class;
        }


        // 声明支持的Excel数据类型（字符串）
        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }
    }
}