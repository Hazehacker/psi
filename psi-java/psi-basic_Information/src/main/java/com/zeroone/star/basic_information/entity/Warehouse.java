package com.zeroone.star.basic_information.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 仓库名称
     */
    private String name;

    /**
     * 仓库编号
     */
    private String number;

    /**
     * 所属组织id
     */
    private String frame;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 地址
     */
    @TableField("`add`")
    private String add;

    /**
     * 备注信息
     */
    private String data;
}
