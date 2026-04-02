package com.zeroone.star.project.dto.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 描述：模板导入dto
 * </p>
 * @author cwznb
 * @version 1.0.0
 */

@Data
@ApiModel("模板导入数据对象")
public class TemplateAddDTO {
    @ApiModelProperty(value = "模板名称",example = "模板名称")
    private String templateName;
    @ApiModelProperty(value = "模板编码",example = "mb001")
    private String templateCode;
    @ApiModelProperty(value="文件存储方式",example = "excel")
    private String saveType;
    @ApiModelProperty(value = "备注",example = "备注")
    private String remark;
    @ApiModelProperty(value = "文件",example = "文件")
    private MultipartFile file;
}
