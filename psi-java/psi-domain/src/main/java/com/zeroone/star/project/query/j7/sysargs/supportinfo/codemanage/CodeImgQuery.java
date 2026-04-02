package com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * 类名：CodeImgQuery
 * 包名：com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage
 * 描述：
 * 作者：star
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@Data
@ApiModel("条码图片")
public class CodeImgQuery {
    @ApiModelProperty(value = "条码ID", required = true, example = "1")
    @NotBlank(message = "条码ID不能为空")
    @Length(max = 50, message = "条码ID长度不能超过50个字符")
    private String id;
}