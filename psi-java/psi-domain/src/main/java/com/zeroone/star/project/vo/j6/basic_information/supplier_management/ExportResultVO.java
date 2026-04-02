package com.zeroone.star.project.vo.j6.basic_information.supplier_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 导出结果VO
 */
@Data
@ApiModel("导出结果")
public class ExportResultVO {
    @ApiModelProperty(value = "文件下载地址")
    private String downloadUrl;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "导出任务ID")
    private String taskId;
}
