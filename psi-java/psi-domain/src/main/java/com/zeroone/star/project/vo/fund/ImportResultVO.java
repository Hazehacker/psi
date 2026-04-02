package com.zeroone.star.project.vo.fund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 描述：导入结果返回视图对象
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author ……
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "导入结果返回视图对象")
public class ImportResultVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总记录数", required = true, example = "100")
    private Integer totalCount;

    @ApiModelProperty(value = "成功记录数", required = true, example = "95")
    private Integer successCount;

    @ApiModelProperty(value = "失败记录数", required = true, example = "5")
    private Integer failureCount;

    @ApiModelProperty(value = "导入耗时（毫秒）", example = "1250")
    private Long costTime;

    @ApiModelProperty(value = "错误详情列表")
    private List<ImportErrorVO> errorList;

    @ApiModelProperty(value = "导入结果摘要信息", example = "导入完成，成功95条，失败5条")
    private String summary;

    /**
     * 便捷方法：计算失败记录数
     */
    public Integer getFailureCount() {
        if (failureCount != null) {
            return failureCount;
        }
        if (totalCount != null && successCount != null) {
            return totalCount - successCount;
        }
        return 0;
    }

    /**
     * 便捷方法：生成摘要信息
     */
    public String getSummary() {
        if (summary != null) {
            return summary;
        }
        return String.format("导入完成，共处理%d条记录，成功%d条，失败%d条", 
            totalCount != null ? totalCount : 0, 
            successCount != null ? successCount : 0, 
            getFailureCount());
    }
}