package com.zeroone.star.project.query.j7.sysargs.supportinfo.inandexpend;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Data
@ApiModel("添加收支类别")
public class IetAddQuery extends PageQuery {
    /**
     * 类别名称
     */
    @ApiModelProperty(value = "类别名称", example = "销售收入")
    private String name;
    /**
     * 收支类型
     */
    @ApiModelProperty(value = "收支类型", example = "0")
    private Integer type;
    /**
     * 排序顺序
     */
    @ApiModelProperty(value = "类别排序", example = "1")
    @NotNull(message = "类别排序不能为空")
    private Integer sort;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "提供服务所得收入")
    private String data;
}