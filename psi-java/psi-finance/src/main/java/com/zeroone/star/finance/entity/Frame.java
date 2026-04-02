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
 * 组织机构
 * </p>
 *
 * @author author
 * @since 2025-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("frame")
public class Frame implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 所属ID
     */
    @TableField("pid")
    private String pid;

    /**
     * 组织名称
     */
    @TableField("name")
    private String name;

    /**
     * 组织排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;


}
