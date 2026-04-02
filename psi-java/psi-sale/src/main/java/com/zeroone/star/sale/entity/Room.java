package com.zeroone.star.sale.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 仓储信息
 * </p>
 *
 * @author author
 * @since 2025-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("room")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 所属仓库
     */
    private String warehouse;

    /**
     * 所属商品
     */
    private String goods;

    /**
     * 辅助属性
     */
    private String attr;

    /**
     * 库存数量
     */
    private BigDecimal nums;


}
