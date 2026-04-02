package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 人员管理表对应的DO类
 * 映射数据库表：people（人员管理）
 * @author 斗气化码
 * @date 2025/11/12
 * Version: 1.0
 */
@Data
@TableName("people")
public class PeopleDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 人员名称
     */
    private String name;

    /**
     * 拼音信息
     */
    private String py;

    /**
     * 人员编号
     */
    private String number;

    /**
     * 所属组织
     */
    private String frame;

    /**
     * 人员性别[0:女|1:男]
     */
    private Integer sex;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 联系地址
     */
    @TableField("`add`")
    private String add;

    /**
     * 身份证号
     */
    private String card;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 扩展信息
     */
    private String more;
}