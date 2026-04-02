package com.zeroone.star.project.query.j2.store;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.query.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-19 13:46
 * @Description: 商品详情查询对象
 * @Version: 1.0
 */
@Data
@ApiModel("InventoryDetailQuery")
public class InventoryDetailQuery extends PageQuery {

    @ApiModelProperty(value = "商品ID(点开详情时的查询条件,内部使用)", example = "1",required = true)
    private String goodsId;

    @ApiModelProperty(value = "商品属性(点开详情时的查询条件,内部使用)", example = "8G+128G")
    private String attrName;

    @ApiModelProperty(value = "单据编号", example = "CGD2510311529121")
    private String documentNumber;

    @ApiModelProperty(value = "单据类型",example = "buy,sell")
    private List<String> documentTypes;

    @ApiModelProperty(value = "仓库ID列表(点开详情时的查询条件,内部使用)",example = "1,2")
    private List<String> warehouseIds;

    @ApiModelProperty(value = "开始日期", example = "2025-10-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期", example = "2025-10-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

}
