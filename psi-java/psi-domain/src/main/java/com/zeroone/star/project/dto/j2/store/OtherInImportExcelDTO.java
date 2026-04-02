package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-29 18:12
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@ApiModel("OtherInImportExcelDTO")
public class OtherInImportExcelDTO {

    @ApiModelProperty(value = "供应商", example = "中创集团")
    private String supplier;

    @ApiModelProperty(value = "单据日期", example = "2020-01-01")
    @DateTimeFormat("yyyy-MM-dd")
    private String time;

    @ApiModelProperty(value = "单据编号", example = "BHdds")
    private String number;

    @ApiModelProperty(value = "单据类型", example = "其他入库单")
    private String type;

    @ApiModelProperty(value = "单据成本", example = "123")
    private String total1;

    @ApiModelProperty(value = "关联人员", example = "张三")
    private String people;

    @ApiModelProperty(value = "物流信息", example = "1234567890")
    private String logistics;

    @ApiModelProperty(value = "备注信息", example = "批量导入")
    private String data1;

    @ApiModelProperty(value = "商品名称", example = "纸张")
    private String goods;

    @ApiModelProperty(value = "辅助属性", example = "A4")
    private String attr;

    @ApiModelProperty(value = "单位", example = "箱")
    private String unit;

    @ApiModelProperty(value = "仓库", example = "二号仓")
    private String warehouse;

    @ApiModelProperty(value = "批次号", example = "BH2020")
    private String batch;

    @ApiModelProperty(value = "生产日期", example = "2020-02-02")
    @DateTimeFormat("yyyy-MM-dd")
    private String mfd;

    @ApiModelProperty(value = "成本", example = "12")
    private BigDecimal price;

    @ApiModelProperty(value = "数量", example = "3")
    private BigDecimal nums;

    @ApiModelProperty(value = "序列号", example = "A1,A2,A3")
    private String serial;

    @ApiModelProperty(value = "总成本", example = "344")
    private BigDecimal total2;

    @ApiModelProperty(value = "备注信息", example = "单据导入")
    private String data2;
}
