package com.zeroone.star.purchase.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 每行的完整映射（第2行同时包含左侧单据字段和右侧商品字段，
 * 第3行以后只有右侧商品字段）。
 *
 * 所有列都映射为 String，读取后由 Service 做类型转换并校验。
 */
@Data
public class CombinedRowExcel {

    @ExcelProperty(index = 0)  private String supplier;        // 供应商
    @ExcelProperty(index = 1)  private String billDate;        // 单据日期
    @ExcelProperty(index = 2)  private String billNumber;      // 单据编号
    @ExcelProperty(index = 3)  private String billTotal;       // 单据金额
    @ExcelProperty(index = 4)  private String actualAmount;    // 实际金额
    @ExcelProperty(index = 5)  private String paidAmount;      // 实付金额
    @ExcelProperty(index = 6)  private String account;         // 结算账户
    @ExcelProperty(index = 7)  private String relatedPeople;   // 关联人员
    @ExcelProperty(index = 8)  private String logistics;       // 物流信息
    @ExcelProperty(index = 9)  private String headerRemark;    // 备注信息（左侧单据备注）

    // 右侧商品列，从 index=10 开始
    @ExcelProperty(index = 10) private String productName;     // 商品名称
    @ExcelProperty(index = 11) private String attr;            // 辅助属性
    @ExcelProperty(index = 12) private String unit;            // 单位
    @ExcelProperty(index = 13) private String warehouse;       // 仓库
    @ExcelProperty(index = 14) private String batchNo;         // 批次号
    @ExcelProperty(index = 15) private String productionDate;  // 生产日期
    @ExcelProperty(index = 16) private String unitPrice;       // 单价
    @ExcelProperty(index = 17) private String quantity;        // 数量
    @ExcelProperty(index = 18) private String serialNo;        // 序列号
    @ExcelProperty(index = 19) private String discountRate;    // 折扣率(%)
    @ExcelProperty(index = 20) private String discountAmount;  // 折扣额
    @ExcelProperty(index = 21) private String itemTotal;       // 金额
    @ExcelProperty(index = 22) private String taxRate;         // 税率(%)
    @ExcelProperty(index = 23) private String taxAmount;       // 税额
    @ExcelProperty(index = 24) private String totalWithTax;    // 价税合计
    @ExcelProperty(index = 25) private String productRemark;   // 备注信息（右侧商品备注）
}
