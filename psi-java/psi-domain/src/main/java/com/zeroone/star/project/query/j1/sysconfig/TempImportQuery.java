package com.zeroone.star.project.query.j1.sysconfig;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  描述：导入模板查询数据对象
 * </p>
 *
 * @auther：nuochezi2cc
 * @version：1.0
 */
@Data
@ApiModel("导入模板查询参数")
public class TempImportQuery extends PageQuery {
    @ApiModelProperty(value = "模板名称关键词，模糊匹配", example = "系统")
    private String templateName;

    @ApiModelProperty(value = "模板编码", example = "UTF-8")
    private String templateCode;

    @ApiModelProperty(value = "文件存储方式（字典值）", example = "excel")
    private String saveType;

    @ApiModelProperty(value = "状态：0=未使用，1=使用中", example = "1")
    private Integer status;
}
