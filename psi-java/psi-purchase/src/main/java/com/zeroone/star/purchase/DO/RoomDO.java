package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 仓储信息表对应的DO类
 * 映射数据库表：room（仓储信息）
 * @author TWTW
 * @date 2025/10/24
 * Version: 1.0
 */
@Data
@TableName("room") // 指定映射的数据库表名
public class RoomDO {

    /**
     * 主键ID
     * 对应表字段：id（varchar(32)，非空）
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 所属仓库
     * 对应表字段：warehouse（varchar(32)，非空）
     */
    private String warehouse;

    /**
     * 所属商品
     * 对应表字段：goods（varchar(32)，非空）
     */
    private String goods;

    /**
     * 辅助属性
     * 对应表字段：attr（varchar(64)，可为空）
     */
    private String attr;

    /**
     * 库存数量
     * 对应表字段：nums（decimal(12,4)，非空）
     * 使用BigDecimal处理高精度小数，避免浮点运算精度丢失
     */
    private BigDecimal nums;
}