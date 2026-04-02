package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 组织机构表对应的DO类
 * 映射数据库表：frame（组织机构）
 * @author Hazenix 
 * @date 2025/11/12
 * Version: 1.0
 */
@Data
@TableName("frame")
public class FrameDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 所属ID
     */
    private String pid;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织排序
     */
    private Integer sort;

    /**
     * 备注信息
     */
    private String data;

}