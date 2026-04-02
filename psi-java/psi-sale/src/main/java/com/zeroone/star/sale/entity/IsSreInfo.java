package com.zeroone.star.sale.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "销售退货单详情表实体（退货商品明细）")
@TableName("sre_info")
public class IsSreInfo {

    @ApiModelProperty(value = "主键ID", example = "550e8400e29b41d4a716446655440000")
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "所属退货单ID（关联is_sre表）", example = "1")
    private String pid;

    @ApiModelProperty(value = "关联销售单详情ID（来源详情，对应is_sell_info表）", example = "25")
    private String source;

    @ApiModelProperty(value = "商品ID（关联is_goods表）", example = "8")
    private String goods;

    @ApiModelProperty(value = "辅助属性（如颜色、规格）", example = "红色,XL")
    private String attr;

    @ApiModelProperty(value = "商品单位", example = "件")
    private String unit;

    @ApiModelProperty(value = "仓库ID（退货入库的仓库，关联is_warehouse表）", example = "3")
    private String warehouse;

    @ApiModelProperty(value = "批次号", example = "B20230901")
    private String batch;

    @ApiModelProperty(value = "生产日期", example = "2023-09-01")
    private LocalDate mfd;

    @ApiModelProperty(value = "退货单价", example = "150.00")
    private BigDecimal price;

    @ApiModelProperty(value = "退货数量", example = "2.00")
    private BigDecimal nums;

    @ApiModelProperty(value = "序列号（多个用逗号分隔）", example = "SN123456,SN123457")
    private String serial;

    @ApiModelProperty(value = "折扣率（百分比）", example = "0.00")
    private BigDecimal discount;

    @ApiModelProperty(value = "折扣额", example = "0.00")
    private BigDecimal dsc;

    @ApiModelProperty(value = "金额（不含税）", example = "300.00")
    private BigDecimal total;

    @ApiModelProperty(value = "税率（百分比）", example = "13.00")
    private BigDecimal tax;

    @ApiModelProperty(value = "税额", example = "39.00")
    private BigDecimal tat;

    @ApiModelProperty(value = "价税合计", example = "339.00")
    private BigDecimal tpt;

    @ApiModelProperty(value = "备注信息", example = "包装完好")
    private String data;
}
