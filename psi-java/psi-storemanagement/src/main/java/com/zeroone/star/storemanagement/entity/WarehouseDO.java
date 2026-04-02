package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

@Data
@TableName(value = "warehouse")
public class WarehouseDO {

    private String id;

    private String name;

    private String number;

    private Integer frame;

    private String contacts;

    private String tel;

    @TableField(value = "`add`")
    private String add;

    private String data;

}
