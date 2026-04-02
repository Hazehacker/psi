package com.zeroone.star.supportinfo.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 商品类别实体类
 * 对应数据库表：category
 */
@Data
@Getter
@Setter
@TableName("category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（整数类型）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属类别（父ID，string类型）
     */
	@TableField("pid")
    private String pid;

    /**
     * 类别名称
     */


    @TableField("name")
    private String name;

    /**
     * 类别排序
     */
	@TableField("sort")
    private Integer sort;

    /**
     * 备注信息
     */

	@TableField("data")
    private String data;


}

