package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: 加减法
 * @CreateTime: 2025-10-26
 * @Description: 序列号领域模型
 * @Version: 1.0
 */
@Data
@TableName("serial")
public class SerialDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 所属仓储
     */
    private String room;

    /**
     * 所属仓库
     */
    private String warehouse;

    /**
     * 所属批次
     */
    private String batch;

    /**
     * 所属商品
     */
    private String goods;

    /**
     * 序列号
     */
    private String number;

    /**
     * 状态[0:未销售|1:已销售|2:已调拨|3:已退货]
     */
    private Integer states;
}
