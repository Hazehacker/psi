package com.zeroone.star.project.dto.j2.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("OtherInListDetailInfoDTO")
public class OtherInListDetailInfoDTO{

    @ApiModelProperty(value = "商品名称",  example = "刀具")
    private String name;

    @ApiModelProperty(value="商品编号",example ="0003")
    private String number;

    @ApiModelProperty(value = "规格型号", example = "DJ-111")
    private String spec;

    @ApiModelProperty(value = "所属商品", example = "1")
    private String goods;

    @ApiModelProperty(value = "辅助属性", example = "1")
    private String attr;

    @ApiModelProperty(value = "单位", example = "个")
    private String unit;

    @ApiModelProperty(value = "仓库", example = "1")
    private String warehouse;

    @ApiModelProperty(value = "批次号", example = "898")
    private String batch;

    @ApiModelProperty(value = "生产日期", example = "2023-10-18")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate mfd;

    @ApiModelProperty(value = "成本", example = "1.0000")
    private BigDecimal price;

    @ApiModelProperty(value = "数量", example = "1.0000")
    private BigDecimal nums;

    @ApiModelProperty(value = "总成本", example = "1.0000")
    private BigDecimal total;

    @ApiModelProperty(value = "备注信息", example = "1")
    private String data;
}
