package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("batch")
public class BatchDO {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @TableField("room")
    private String room;

    @TableField("warehouse")
    private String warehouse;

    @TableField("goods")
    private String goods;

    @TableField("number")
    private String number;

    @TableField("time")
    private LocalDateTime time;

    @TableField("nums")
    private BigDecimal nums;

}
