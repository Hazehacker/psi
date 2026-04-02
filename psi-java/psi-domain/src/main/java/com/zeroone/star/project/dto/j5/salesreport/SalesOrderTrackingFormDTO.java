package com.zeroone.star.project.dto.j5.salesreport;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderTrackingFormDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 销售订单跟踪表对象类
 *
 * @author leyu
 * @date 2025-10-19
 */

@Data
@ApiModel("销售订单跟踪表数据对象")
public class SalesOrderTrackingFormDTO {

    // Type=0 父级 (单据) / Type=1 子级 (单据行)
    @JsonIgnore
    @ApiModelProperty(value = "用于MyBatis分组的唯一ID (单据ID)", hidden = true)
    private String orderId;
    @ApiModelProperty(value = "所属组织名称")
    private String frameName;
    @ApiModelProperty(value = "客户")
    private String customerName;
    @ApiModelProperty(value = "单据时间")
    private String time;
    @ApiModelProperty(value = "单据编号")
    private String number;
    @ApiModelProperty(value = "单据入库状态")
    private Integer state;

    // Type=1 父级 (商品组) / Type=0 子级 (商品行)
    @JsonIgnore
    @ApiModelProperty(value = "用于MyBatis分组的唯一ID (商品组Key)", hidden = true)
    private String groupKey;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "辅助属性")
    private String attr;
    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;
    @ApiModelProperty(value = "商品ID")
    private String goods;
    @ApiModelProperty(value = "仓库ID")
    private String warehouse;

    // Type=0 子级 / Type=1 子级 (明细)
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "单价")
    private String price;
    @ApiModelProperty(value = "数量")
    private Integer nums;
    @ApiModelProperty(value = "金额 (明细)")
    private BigDecimal total;
    @ApiModelProperty(value = "备注信息")
    private String itemRemark;
    @ApiModelProperty(value = "明细入库状态")
    private Integer itemState;
    @ApiModelProperty(value = "未入库数量 (明细)")
    private Integer unstockedQuantity;
    @ApiModelProperty(value = "未入库金额 (明细)")
    private BigDecimal unstockedAmount;

    // 汇总(父级)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "到货日期")
    private LocalDate arrival;
    @ApiModelProperty(value = "总金额 (汇总)")
    private BigDecimal totalAmount;
    @ApiModelProperty(value = "总未入库数量 (汇总)")
    private Integer totalUnstockedQuantity;
    @ApiModelProperty(value = "总未入库金额 (汇总)")
    private BigDecimal totalUnstockedAmount;

    // 子列表
    @ApiModelProperty(value = "子项列表")
    private List<SalesOrderTrackingFormDTO> items;

}
