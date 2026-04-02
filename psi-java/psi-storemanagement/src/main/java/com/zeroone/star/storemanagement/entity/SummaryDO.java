package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * @author Coda
 * @date 2025/10/28
 * @description 收发统计表
 */
@Data
@TableName("summary")
public class SummaryDO {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private String pid;

    private String type;

    @TableField("class")
    private String cls;

    private String info;

    private LocalDateTime time;

    private String goods;

    private String attr;

    private String warehouse;

    private String batch;

    private LocalDate mfd;

    private String serial;

    private int direction;

    private BigDecimal price;

    private BigDecimal nums;

    private BigDecimal uct;

    private BigDecimal bct;

    private String exist;

    private String balance;

    private String handle;

}
