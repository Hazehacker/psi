package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author 唐怀瑟
 * @Data 2025/10/24 23:16
 * @PackageName: com.zeroone.star.purchase.DO
 * @CLASSNAME: WarehouseDO
 * @Description: 仓库
 * @Version 1.0
 */
@Getter
@Setter
@TableName("warehouse") // 指定对应的数据表名
public class WarehouseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
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
     * 所属组织
     */
    private String frame = "0";

    /**
     * 联系人员
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 仓库地址
     */
    @TableField("`add`")
    private String add;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
