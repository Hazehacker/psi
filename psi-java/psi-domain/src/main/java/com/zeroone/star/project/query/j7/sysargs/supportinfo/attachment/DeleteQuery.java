package com.zeroone.star.project.query.j7.sysargs.supportinfo.attachment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 类名：DeleteQuery
 * 包名：com.zeroone.star.project.query.j7.sysargs.supportinfo.attachment
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/22
 * 版本号：V1.0
 */
@Getter
@Setter
@ApiModel("附件删除查询参数")
public class DeleteQuery {

    @NotNull(message = "附件ID不能为空")
    @ApiModelProperty(value = "附件唯一标识", required = true, example = "123123")
    protected String id;
}
