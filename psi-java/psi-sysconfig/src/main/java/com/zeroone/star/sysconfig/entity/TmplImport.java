package com.zeroone.star.sysconfig.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 导入模板
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Getter
@Setter
@TableName("tmpl_import")
public class TmplImport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板编码
     */
    private String code;

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
