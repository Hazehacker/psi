package com.zeroone.star.project.dto.j1.sysconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 描述：模板修改dto
 * </p>
 * @author cwznb
 * @version 1.0.0
 */

@Data
@ApiModel("模板修改数据对象")
public class TemplateUpdateDTO extends TemplateAddDTO{
    @ApiModelProperty(value = "模板id",example = "模板id")
    private String id;
    @ApiModelProperty(value = "模板存储路径",example = "模板存储路径")
    private String savePath;
    @ApiModelProperty(value = "数据状态",example = "1")
    private Integer status;

}
