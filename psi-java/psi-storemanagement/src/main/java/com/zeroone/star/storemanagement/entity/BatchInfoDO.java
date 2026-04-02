package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("batch_info")
public class BatchInfoDO {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String pid;

    private String type;

    @TableField("class")
    private String cls;

    private String info;

    private int direction;

    private BigDecimal nums;

}
