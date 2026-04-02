package com.zeroone.star.sale.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 仓储详情
 * </p>
 *
 * @author author
 * @since 2025-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("room_info")
public class RoomInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属仓储
     */
    private String pid;

    /**
     * 单据类型
     */
    private String type;

    /**
     * 所属类
     */
    @TableField("`class`")  // 使用反引号转义
    private String className;

    /**
     * 所属详情
     */
    private String info;

    /**
     * 单据时间
     */
    private LocalDateTime time;

    /**
     * 方向[0:出|1:入]
     */
    private Integer direction;

    /**
     * 基础单价
     */
    private BigDecimal price;

    /**
     * 基础数量
     */
    private BigDecimal nums;


}
