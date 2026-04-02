package com.zeroone.star.project.dto.j3.capital;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: junjie
 * @CreateTime: 2025-10-18
 * @Description: 核销单批量删除DTO
 * @Version: 1.0
 */
@Data
@ApiModel("核销单批量删除DTO")
public class VerificationBatchDeleteDTO {
    
    @ApiModelProperty(value = "核销单ID列表", required = true, example = "[\"1\", \"2\", \"3\"]")
    @NotEmpty(message = "核销单ID列表不能为空")
    private List<String> ids;
}
