package com.zeroone.star.supportinfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 收支类别
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("iet")
public class Iet implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 类别名称
     */
    @TableField("name")
    private String name;

    /**
     * 收支类型[0:收入:1支出]
     */
    @TableField("type")
    private Integer type;

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
