package com.zeroone.star.project.query.j3.capital;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: junjie
 * @CreateTime: 2025-10-18
 * @Description: 核销单导入查询参数
 * @Version: 1.0
 */
@Data
@ApiModel("核销单导入查询参数")
public class VerificationImportQuery {
    
    @ApiModelProperty(value = "是否覆盖已有数据", example = "false")
    private Boolean overwrite = false;
    
    @ApiModelProperty(value = "是否跳过错误行", example = "true")
    private Boolean skipError = true;
    
    @ApiModelProperty(value = "所属组织", example = "01")
    private String frame;
}
