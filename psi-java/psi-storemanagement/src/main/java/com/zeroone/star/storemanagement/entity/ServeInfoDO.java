package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("serve_info")
public class ServeInfoDO {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String pid;

    private String type;

    @TableField("class")
    private String cls;

    private String info;

    private LocalDateTime time;

    private BigDecimal price;

    private BigDecimal nums;
}
