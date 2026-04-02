package com.zeroone.star.project.dto.j6.basic_information.product_management;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author 杨潇, nick8311370879
 * @version 1.0
 * @date:
 */
@Data
@ApiModel("商品导入数据传输对象")
public class GoodsExcelDTO {

    @ExcelProperty("商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ExcelProperty("商品编号")
    @NotBlank(message = "商品编号不能为空")
    private String number;

    @ExcelProperty("商品型号")
    private String spec;

    @ExcelProperty("商品类别")
    @NotBlank(message = "商品类别不能为空")
    private String category;

    @ExcelProperty("商品品牌")
    private String brand;

    @ExcelProperty("商品单位")
    private String unit;

    @ExcelProperty("购货价格")
    @NotNull(message = "购货价格不能为空")
    private BigDecimal buy;

    @ExcelProperty("销货价格")
    @NotNull(message = "销货价格不能为空")
    private BigDecimal sell;

    @ExcelProperty("零售价格")
    @NotNull(message = "零售价格不能为空")
    private BigDecimal retailPrice;

    @ExcelProperty("兑换积分")
    private Integer exchangePoints;

    @ExcelProperty("商品条码")
    private String code;

    @ExcelProperty("商品货位")
    private String location;

    @ExcelProperty("库存阈值")
    @NotNull(message = "库存阈值不能为空")
    private BigDecimal stock;

    @ExcelProperty("商品类型")
    @NotNull(message = "商品类型不能为空")
    private Integer type;

    @ExcelProperty("备注信息")
    private String data;
}
