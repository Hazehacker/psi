package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author vigorous
 * @date 2025/10/23
 * @description 人员
 */
@Data
@TableName(value = "people")
public class PeopleDO {

    private String id;

    private String name;

    private String py;

    private String number;

    private String tel;

    private String frame;

    private Integer sex;

    @TableField(value = "`add`")
    private String add;

    private String card;

    private String data;

    private String more;
}
