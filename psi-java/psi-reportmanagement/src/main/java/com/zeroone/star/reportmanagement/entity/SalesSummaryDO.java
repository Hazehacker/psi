package com.zeroone.star.reportmanagement.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 接收数据库查询到的结果
 * @author taohu5564
 * @date 2025-10-25
 */
@Data
public class SalesSummaryDO {
    /* 分组依据 */
    private String groupField;

    /** 商品名称 */
    private String productName;

    /** 辅助属性（如颜色、尺寸） */
    private String attribute;

    /** 仓库名称 */
    private String warehouse;

    private String unit;

    // ========== 销售单汇总 ==========
    /**
     * 销售单价
     */
    private BigDecimal salesPrice;
    /**
     * 销售单中的销售数量
     */
    private Integer salesCount;

    // 金额
    private BigDecimal salesTotalPrice;


    // ========== 退货汇总 ==========
    /**
     * 退货单价
     */
    private BigDecimal returnPrice;
    /**
     * 退货总数量
     */
    private Integer returnCount;

    private BigDecimal returnTotalPrice;

}
