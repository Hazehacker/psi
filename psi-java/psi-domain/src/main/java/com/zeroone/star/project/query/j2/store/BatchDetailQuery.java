package com.zeroone.star.project.query.j2.store;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.query.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-19 10:41
 * @Description: 批次查询详情参数对象
 * @Version: 1.0
 */
@Data
@ApiModel("BatchDetailQuery")
public class BatchDetailQuery extends PageQuery {
    @ApiModelProperty(value = "批次pid(点开详情时的查询条件,内部使用)", example = "1", required = true)
    private List<String> pid;

    @ApiModelProperty(value = "单据编号", example = "QTRKD2510181347194")
    private String number;

    @ApiModelProperty(value = "单据类型", example = "采购单")
    private List<String> type;

    @ApiModelProperty(value = "开始日期", example = "2025-10-13")
    private String startTime;

    @ApiModelProperty(value = "结束日期", example = "2025-10-22")
    private String endTime;

    @ApiModelProperty(value = "仓库信息(内部使用)", example = "1,2", required = true)
    private List<String> warehouseIds;
}
