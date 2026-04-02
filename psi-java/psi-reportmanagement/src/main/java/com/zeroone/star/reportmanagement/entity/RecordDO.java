package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 单据记录数据对象
 *
 * @author 言语
 * @since 2025/10/23
 */

@Getter
@Setter
@TableName("record")
public class RecordDO {

    /**
     * 主键ID
     */
    @TableId()
    private String id;

    /**
     * 单据类型
     */
    @TableField("type")
    private String type;

    /**
     * 关联单据
     */
    @TableField("source")
    private String source;

    /**
     * 事件时间
     */
    @TableField("time")
    private LocalDateTime time;

    /**
     * 事件用户
     */
    @TableField("user")
    private String user;

    /**
     * 事件内容
     */
    @TableField("info")
    private String info;

}