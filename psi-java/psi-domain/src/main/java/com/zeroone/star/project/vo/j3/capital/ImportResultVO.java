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
 * @Description: 导入结果VO
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("导入结果VO")
public class ImportResultVO {
    
    @ApiModelProperty(value = "导入成功数量", example = "50")
    private Integer successCount;
    
    @ApiModelProperty(value = "导入失败数量", example = "5")
    private Integer failCount;
    
    @ApiModelProperty(value = "总行数", example = "55")
    private Integer totalCount;
    
    @ApiModelProperty(value = "失败行号列表", example = "[3, 10, 25]")
    private List<Integer> failRows;
    
    @ApiModelProperty(value = "失败原因列表")
    private List<String> failReasons;
    
    @ApiModelProperty(value = "导入耗时(毫秒)", example = "1500")
    private Long duration;
}
