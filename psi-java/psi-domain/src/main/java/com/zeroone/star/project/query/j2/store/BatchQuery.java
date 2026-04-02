package com.zeroone.star.project.query.j2.store;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.query.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 10:37
 * @Description: 批次查询参数对象
 * @Version: 1.0
 */
@Data
@ApiModel("BatchQuery")
public class BatchQuery extends PageQuery {

    @ApiModelProperty(value = "商品名称",example = "1")
    private String  goodsName;

    @ApiModelProperty(value = "商品编号",example = "0001")
    private String goodsNumber;

    @ApiModelProperty(value = "批次号码",example = "20231001")
    private String batchNumber;

    @ApiModelProperty(value = "生产日期",example = "2025-10-15")
    private LocalDate productDate;

    @ApiModelProperty(value = "商品型号",example = "500ml")
    private String goodsSpec;

    @ApiModelProperty(value = "商品类别",example = "1")
    private String  goodsCategoryId;

    @ApiModelProperty(hidden = true)
    private List<String> goodsCategoryIds;//类别树ID列表(内部处理字段)

    @ApiModelProperty(value = "商品品牌",example = "苹果")
    private String goodsBrand;

    @ApiModelProperty(value = "商品条码",example = "111222")
    private String goodsCode;

    @ApiModelProperty(value = "仓库信息",example = "1,2")
    private List<String> warehouseIds;


    @ApiModelProperty(
            value = "批次类型：0-常规批次，1-预警批次",
            example = "1")
    private Integer batchState;
}
