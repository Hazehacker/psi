package com.zeroone.star.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 人员管理
 * </p>
 *
 * @author author
 * @since 2025-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("people")
public class People implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 人员名称
     */
    @TableField("name")
    private String name;

    /**
     * 拼音信息
     */
    @TableField("py")
    private String py;

    /**
     * 人员编号
     */
    @TableField("number")
    private String number;

    /**
     * 所属组织
     */
    @TableField("frame")
    private String frame;

    /**
     * 人员性别[0:女|1:男]
     */
    @TableField("sex")
    private Boolean sex;

    /**
     * 联系电话
     */
    @TableField("tel")
    private String tel;

    /**
     * 联系地址
     */
    @TableField("add")
    private String add;

    /**
     * 身份证号
     */
    @TableField("card")
    private String card;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;

    /**
     * 扩展信息
     */
    @TableField("more")
    private String more;


}
