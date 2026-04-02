package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: 加减法
 * @CreateTime: 2025-10-26
 * @Description: 仓储详情领域模型
 * @Version: 1.0
 */
@Data
@TableName("room_info")
public class RoomInfoDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 所属仓储ID
     */
    private String pid;

    /**
     * 单据类型
     */
    private String type;

    /**
     * 所属类
     * 对应表字段 class
     */
    private String classify;

    /**
     * 所属详情
     */
    private String info;

    /**
     * 单据时间
     */
    private String time;

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
    private Integer nums;
}
