package com.zeroone.star.purchase.DO;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 源单信息
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-25
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sor_info")
public class SorInfoDO {

    /**
     * 销售订单id
     */
    @TableId(value = "id",type= IdType.AUTO)
    @NotNull(message = "销售订单id不能为空")
    private String id;

    /**
     * 所属id
     */
    @NotNull(message = "所属id不能为空")
    private String pid;

    /**
     * 所属商品
     */
    @NotNull(message = "所属商品不能为空")
    private String goods;

    /**
     * 辅助属性
     */
    private String attr;

    /**
     * 单位
     */
    @NotNull(message = "单位不能为空")
    private String unit;

    /**
     * 仓库
     */
    private String warehouse;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    private BigDecimal nums;

    /**
     * 折扣率
     */
    @NotNull(message = "折扣率不能为空")
    private BigDecimal discount;;

    /**
     * 折扣额
     */
    @NotNull(message = "折扣额不能为空")
    private BigDecimal dsc;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    private BigDecimal total;

    /**
     * 税率
     */
    @NotNull(message = "税率不能为空")
    private BigDecimal tax;

    /**
     * 税额
     */
    @NotNull(message = "税额不能为空")
    private BigDecimal tat;

    /**
     * 价税合计
     */
    @NotNull(message = "价税合计不能为空")
    private BigDecimal tpt;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 出库数量
     */
    private BigDecimal handle;
}
