package com.zeroone.star.project.vo.fund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 描述：导入错误详情视图对象
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author ……
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "导入错误详情视图对象")
public class ImportErrorVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "错误行号（Excel中的行号）", required = true, example = "5")
    private Integer rowNumber;

    @ApiModelProperty(value = "错误字段名", example = "customer")
    private String fieldName;

    @ApiModelProperty(value = "错误值", example = "12345")
    private String errorValue;

    @ApiModelProperty(value = "错误原因", required = true, example = "客户不存在")
    private String errorMessage;

    @ApiModelProperty(value = "错误类型", example = "数据校验失败")
    private String errorType;
}