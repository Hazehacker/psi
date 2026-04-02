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
 * 条码
 * </p>
 *
 * @author hh
 * @since 2025-10-23
 */
@Getter
@Setter
@TableName("code")
public class Code implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 条码名称
     */
    @TableField("name")
    private String name;

    /**
     * 条码内容
     */
    @TableField("info")
    private String info;

    /**
     * 条码类型[0:条形码 | 1:二维码]
     */
    @TableField("type")
    private Integer type;

    /**
     * 备注信息
     */
    @TableField("data")
    private String data;


}
