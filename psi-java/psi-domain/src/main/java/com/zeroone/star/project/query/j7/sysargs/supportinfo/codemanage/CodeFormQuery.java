package com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 类名：CodeFormQuery
 * 包名：com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@Data
@ApiModel("条码列表")
public class CodeFormQuery extends PageQuery {
    @ApiModelProperty(value = "条码名称", example = "测试条码")
    @Size(max = 100, message = "条码名称长度不能超过100个字符")
    private String name;

    @ApiModelProperty(value = "条码描述", example = "https://www.baidu.com")
    @Size(max = 200, message = "条码描述长度不能超过200个字符")
    private String info;

    @ApiModelProperty(value = "条码类型", example = "1", allowableValues = "0,1")
    private Integer type;

    @ApiModelProperty(value = "备注信息", example = "产品信息二维码")
    @Size(max = 500, message = "备注信息长度不能超过500个字符")
    private String data;
}