package com.zeroone.star.project.vo.j3.capital;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: junjie
 * @CreateTime: 2025-10-18
 * @Description: 批量操作结果VO
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("批量操作结果VO")
public class BatchOperateResultVO {
    
    @ApiModelProperty(value = "成功数量", example = "10")
    private Integer successCount;
    
    @ApiModelProperty(value = "失败数量", example = "2")
    private Integer failCount;
    
    @ApiModelProperty(value = "总数量", example = "12")
    private Integer totalCount;
    
    @ApiModelProperty(value = "失败的ID列表", example = "[\"1\", \"3\"]")
    private List<String> failIds;
    
    @ApiModelProperty(value = "失败原因列表")
    private List<String> failReasons;
}
