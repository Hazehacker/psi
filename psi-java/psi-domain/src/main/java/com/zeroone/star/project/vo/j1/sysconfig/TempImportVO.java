package com.zeroone.star.project.vo.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  描述：导入模板视图对象 用于分页查询
 * </p>
 *
 * @author：nuochezi2cc
 * @version:1.0
 */
@Data
@ApiModel("导入模板列表")
public class TempImportVO {
    @ApiModelProperty(value = "模板ID", example = "模板ID")
    private String id;

    @ApiModelProperty(value = "模板名称", example = "系统参数模板")
    private String templateName;

    @ApiModelProperty(value = "模板编码", example = "UTF-8")
    private String templateCode;

    @ApiModelProperty(value = "文件存储方式", example = "excel")
    private String saveType;

    @ApiModelProperty(value = "备注", example = "用于参数信息批量导入")
    private String remark;

    @ApiModelProperty(value = "状态：0=未使用，1=使用中", example = "1")
    private Integer status;

}
