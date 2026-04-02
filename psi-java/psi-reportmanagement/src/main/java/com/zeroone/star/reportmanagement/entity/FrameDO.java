package com.zeroone.star.reportmanagement.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 组织机构表
 * @author qzk
 * @date 2025/10/23
 */
@Getter
@Setter
@TableName("frame")
public class FrameDO {

    /**
     * 唯一id
     */
    @TableId(value = "id")
    private String id;

    /**
     *所属组织
     */
    @TableField(value = "pid")
    private String pid;

    /**
     *组织名称
     */
    @TableField(value = "name")
    private String name;

    /**
     *组织排序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     *备注信息
     */
    @TableField(value = "data")
    private String data;

}
