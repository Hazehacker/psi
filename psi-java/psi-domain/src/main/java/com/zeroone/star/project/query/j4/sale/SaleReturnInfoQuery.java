package com.zeroone.star.project.query.j4.sale;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Api("销售退货单详情查询")
public class SaleReturnInfoQuery {

    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    @ApiModelProperty(value = "销售退货单主表ID", example = "1")
    private Long pid;
    @ApiModelProperty(value = "商品名称", example = "xx")
    private String goods;
    @ApiModelProperty(value = "辅助属性", example = "xx")
    private String attr;
    @ApiModelProperty(value = "单位", example = "个")
    private String unit;
    @ApiModelProperty(value = "仓库名称", example = "xx")
    private Long warehouse;
    @ApiModelProperty(value = "批次号", example = "xxx")
    private String batch;
    @ApiModelProperty(value = "生产日期", example = "2356478")
    private Long mfd;
    @ApiModelProperty(value = "序列号", example = "xxx")
    private String serial;
    @ApiModelProperty(value = "备注信息", example = "xxx")
    private String data;

}
