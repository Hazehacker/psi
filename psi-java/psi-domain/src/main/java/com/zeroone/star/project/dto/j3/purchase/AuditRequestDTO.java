package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author 唐怀瑟
 * @Data 2025/10/30 22:15
 * @PackageName: com.zeroone.star.project.dto.j3.purchase
 * @CLASSNAME: AuditRequestDTO
 * @Description: 封装审核/反审核请求参数
 * @Version 1.0
 */
@Data
@ApiModel("审核请求参数")
public class AuditRequestDTO {
    @ApiModelProperty(value = "操作类型：1-审核 0-反审核", example = "1", required = true)
    @NotNull(message = "操作类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "审核ID列表", required = true)
    @NotEmpty(message = "审核ID列表不能为空")
    private List<Long> ids;
}
