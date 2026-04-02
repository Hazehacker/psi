package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "frame")
public class FrameDO {
    private String id;

    private String pid;

    private String name;

    private int sort;

    private String data;
}
