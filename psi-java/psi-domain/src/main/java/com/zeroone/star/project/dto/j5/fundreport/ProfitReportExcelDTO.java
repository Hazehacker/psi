package com.zeroone.star.project.dto.j5.fundreport;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 利润表Excel导出数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ContentRowHeight(20)
@HeadRowHeight(25)
public class ProfitReportExcelDTO {
    @ExcelProperty("单据类型")
    @ColumnWidth(15)
    private String projectName;
    @ExcelProperty("金额")
    @ColumnWidth(15)
    private String amount;
}