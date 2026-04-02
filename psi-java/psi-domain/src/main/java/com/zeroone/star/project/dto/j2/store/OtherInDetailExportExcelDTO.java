package com.zeroone.star.project.dto.j2.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("OtherInDetailExportExcelDTO")
public class OtherInDetailExportExcelDTO {
    @ApiModelProperty(value = "商品名称", example = "纸张")
    private String goodName;

    @ApiModelProperty(value = "规格型号", example = "12")
    private String spec;

    @ApiModelProperty(value = "辅助属性", example = "1")
    private String attr;

    @ApiModelProperty(value = "单位", example = "个")
    private String unit;

    @ApiModelProperty(value = "仓库", example = "一号仓")
    private String warehouse;

    @ApiModelProperty(value = "批次号", example = "898")
    private String batch;

    @ApiModelProperty(value = "成本", example = "1.0000")
    private String price;

    @ApiModelProperty(value = "数量", example = "1.0000")
    private String nums;

    @ApiModelProperty(value = "序列号", example = "1")
    private String serial;

    @ApiModelProperty(value = "总成本", example = "1.0000")
    private String total;

    @ApiModelProperty(value = "备注信息", example = "1")
    private String data;
}
