package com.zeroone.star.oauth2.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 附件
 * </p>
 *
 * @author heavydrink
 * @since 2025-11-11
 */
@Getter
@Setter
public class Annex implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型，值对应字典表value
     */
    private String fileType;

    /**
     * 文件存储方式，值对应字典表value
     */
    private String saveType;

    /**
     * 文件存储路径，不要将服务器域名和端口存储到数据库
     */
    private String savePath;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据状态 0 未使用 1使用中
     */
    private Integer status;


}
