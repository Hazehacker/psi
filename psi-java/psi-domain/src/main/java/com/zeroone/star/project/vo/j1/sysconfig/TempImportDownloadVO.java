package com.zeroone.star.project.vo.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  描述：导入模板视图对象 用于下载模板
 * </p>
 *
 * @author：nuochezi2cc
 * @version:1.0
 */
@Data
@ApiModel("导入模板下载")
public class TempImportDownloadVO {
    @ApiModelProperty(value = "模板ID", example = "模板id")
    private String id;

    @ApiModelProperty(value = "模板名称", example = "系统参数模板")
    private String templateName;

    @ApiModelProperty(value = "模板编码", example = "17904ad5-f37b-4a87-b69b-85e2a8a146d3")
    private String templateCode;

    @ApiModelProperty(value = "文件存储方式", example = "excel")
    private String saveType;

    @ApiModelProperty(value = "下载URL", example = "xxx/uploads/templates/param_template.xlsx")
    private String downloadUrl;

    @ApiModelProperty(value = "备注信息", example = "用于用户导入的模板")
    private String remark;

    @ApiModelProperty(value = "状态（0-禁用，1-启用）", example = "1")
    private Integer status;
}
