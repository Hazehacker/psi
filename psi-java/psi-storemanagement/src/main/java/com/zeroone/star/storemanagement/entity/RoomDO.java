package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("room")
public class RoomDO {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String warehouse;

    private String goods;

    private String attr;

    private BigDecimal nums;
}
