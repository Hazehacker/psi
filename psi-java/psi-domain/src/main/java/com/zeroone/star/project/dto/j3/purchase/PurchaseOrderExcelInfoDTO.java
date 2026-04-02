package com.zeroone.star.project.dto.j3.purchase;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 导出采购订单详细报表DTO
 * @author: 加减法
 * @date: 2023/5/19 11:04
 */
@Data
@ApiModel("采购订单详细报表DTO")
@HeadRowHeight(25) // 设置表头行高
@ColumnWidth(20)   // 设置默认列宽
public class PurchaseOrderExcelInfoDTO {

    /**
     * 供应商
     */
    @ExcelProperty(value = "供应商", index = 0)
    @NotBlank(message = "供应商不能为空")
    @ApiModelProperty(value = "供应商", example = "张三", required = true)
    private String supplier;

    /**
     * 单据日期
     * 对应数据库bor字段time
     */
    @ExcelProperty(value = "单据日期", index = 1)
    @NotBlank(message = "单据日期不能为空")
    @ApiModelProperty(value = "单据日期", example = "2023-05-19", required = true)
    private String dateTime;

    /**
     * 单据编号
     * 对应数据库bor字段number
     */
    @ExcelProperty(value = "单据编号", index = 2)
    @NotBlank(message = "单据编号不能为空")
    @ApiModelProperty(value = "单据编号", example = "1", required = true)
    private String billNum;

    /**
     * 所属商品
     */
    @ExcelProperty(value = "所属商品", index = 3)
    @NotBlank(message = "所属商品不能为空")
    @ApiModelProperty(value = "所属商品", example = "小刀", required = true)
    private String goods;

    /**
     * 商品编号
     * 对应数据库goods字段number
     */
    @ExcelProperty(value = "商品编号", index = 4)
    @ApiModelProperty(value = "商品编号", example = "0001")
    private String goodsNum;

    /**
     * 规格型号
     */
    @ExcelProperty(value = "规格型号", index = 5)
    @ApiModelProperty(value = "规格型号", example = "DJ-0001")
    private String spec;

    /**
     * 辅助属性
     */
    @ExcelProperty(value = "辅助属性", index = 6)
    @ApiModelProperty(value = "辅助属性", example = "红色")
    private String attr;

    /**
     * 商品单位
     */
    @ExcelProperty(value = "商品单位", index = 7)
    @NotBlank(message = "商品单位不能为空")
    @ApiModelProperty(value = "商品单位", example = "kg", required = true)
    private String unit;

    /**
     * 仓库
     */
    @ExcelProperty(value = "仓库", index = 8)
    @NotBlank(message = "仓库不能为空")
    @ApiModelProperty(value = "仓库", example = "1号仓库", required = true)
    private String warehouse;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价", index = 9)
    @NotNull(message = "单价不能为空")
    @ApiModelProperty(value = "单价", example = "10.0", required = true)
    private BigDecimal price;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量", index = 10)
    @NotNull(message = "数量不能为空")
    @ApiModelProperty(value = "数量", example = "1.0", required = true)
    private BigDecimal nums;

    /**
     * 入库数量
     */
    @ExcelProperty(value = "入库数量", index = 11)
    @ApiModelProperty(value = "入库数量", example = "1")
    private BigDecimal inNums;

    /**
     * 折扣率
     */
    @ExcelProperty(value = "折扣率", index = 12)
    @ApiModelProperty(value = "折扣率", example = "0.8")
    private BigDecimal discount;

    /**
     * 折扣额
     */
    @ExcelProperty(value = "折扣额", index = 13)
    @ApiModelProperty(value = "折扣额", example = "1")
    private BigDecimal dsc;

    /**
     * 金额
     */
    @ExcelProperty(value = "金额", index = 14)
    @ApiModelProperty(value = "金额", example = "10.0")
    private BigDecimal commonTotal;

    /**
     * 单据金额
     * 对应数据库bor字段total
     */
    @ExcelProperty(value = "单据金额", index = 15)
    @ApiModelProperty(value = "单据金额", example = "10.0")
    private BigDecimal billTotal;

    /**
     * 实际金额
     * 对应数据库bor字段actual
     */
    @ExcelProperty(value = "实际金额", index = 16)
    @ApiModelProperty(value = "实际金额", example = "10.0")
    private BigDecimal actualTotal;

    /**
     * 关联人员
     */
    @ExcelProperty(value = "关联人员", index = 17)
    @ApiModelProperty(value = "关联人员", example = "1")
    private String people;

    /**
     * 到货日期
     */
    @ExcelProperty(value = "到货日期", index = 18)
    private String arrival;

    /**
     * 物流信息
     */
    @ExcelProperty(value = "物流信息", index = 19)
    private String logistics;

    /**
     * 备注信息
     * 对应数据库bor字段data
     */
    @ExcelProperty(value = "备注信息", index = 20)
    @ColumnWidth(40) // 单独给备注设置更宽的列
    @ApiModelProperty(value = "备注信息", example = "无")
    private String remarkData;
}
