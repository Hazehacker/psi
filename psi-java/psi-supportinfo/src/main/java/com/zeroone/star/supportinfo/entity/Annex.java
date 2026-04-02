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
 * 附件
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("annex")
public class Annex implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 文件名称
     */
    @TableField("name")
    private String name;

    /**
     * 文件类型，值对应字典表value
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 文件存储方式，值对应字典表value
     */
    @TableField("save_type")
    private String saveType;

    /**
     * 文件存储路径，不要将服务器域名和端口存储到数据库
     */
    @TableField("save_path")
    private String savePath;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 数据状态 0 未使用 1使用中
     */
    @TableField("status")
    private Integer status;


}
