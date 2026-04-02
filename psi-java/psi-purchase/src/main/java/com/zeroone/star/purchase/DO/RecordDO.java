package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 单据记录表对应的DO类
 * 映射数据库表：record
 * @author TWTW
 * @date 2025/10/24
 * Version: 1.0
 */
@Data
@TableName("record") // 指定对应的数据表名
public class RecordDO {

    /**
     * 主键ID
     * 对应表字段：id（varchar(32)，非空）
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 单据类型
     * 对应表字段：type（varchar(32)，非空）
     */
    private String type;

    /**
     * 关联单据
     * 对应表字段：source（varchar(32)，非空）
     */
    private String source;

    /**
     * 事件时间
     * 对应表字段：time（datetime，非空）
     * 使用LocalDateTime（Java 8+）替代Date，更推荐处理时间
     */
    private LocalDateTime time;

    /**
     * 事件用户
     * 对应表字段：user（varchar(32)，非空）
     */
    private String user;

    /**
     * 事件内容
     * 对应表字段：info（varchar(64)，非空）
     */
    private String info;
}