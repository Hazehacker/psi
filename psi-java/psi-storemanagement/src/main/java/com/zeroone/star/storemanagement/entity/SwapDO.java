package com.zeroone.star.storemanagement.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SwapDO {
    //给id字段配置自定义的雪花算法生成策略
    @TableId(type = IdType.NONE)
    private String id;

    private String frame;

    private LocalDateTime time;

    private String number;

    private BigDecimal total;

    private BigDecimal cost;

    private String logistics;

    private String people;

    private String file;

    private String data;

    private String more;

    private int examine;

    private int cse;

    private String user;

    //给不能为空但是插入时dto没有的字段设置默认值
    public void setValue() {
        this.frame = "默认组织";
        this.user = "默认制单人";
    }

}