package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: a
 * @CreateTime: 2025-10-25
 * @Description: 属性实体类
 * @Version: 1.0
 */
@Getter
@Setter
@TableName("attribute")
public class AttributeDO {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性排序
     */
    private String sort;

    /**
     * 备注信息
     */
    private String data;
}
