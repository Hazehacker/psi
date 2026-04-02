package com.zeroone.star.project.dto.j3.capital;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: 小夏
 * @CreateTime: 2025-10-30
 * @Description: 核销单操作统一DTO（核销、反核销）
 * @Version: 1.0
 */
@Data
@ApiModel("核销单操作DTO")
public class VerificationOperationDTO {
    
    @ApiModelProperty(value = "操作类型：VERIFY-核销, UNVERIFY-反核销", required = true, example = "VERIFY")
    @NotNull(message = "操作类型不能为空")
    private String type;
    
    @ApiModelProperty(value = "核销单ID列表", required = true, example = "[\"1\", \"2\", \"3\"]")
    @NotEmpty(message = "核销单ID列表不能为空")
    private List<String> ids;
    
    @ApiModelProperty(value = "操作备注", example = "批量核销")
    private String remark;
}
