package com.zeroone.star.project.dto.j2.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-19 13:53
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@ApiModel("OtherInListInfoDTO")
@Builder
public class OtherInListInfoDTO {

    @ApiModelProperty(value = "所属商品", example = "1")
    private String goods;

    @ApiModelProperty(value = "辅助属性", example = "1")
    private String attr;

    @ApiModelProperty(value = "单位", example = "个")
    private String unit;

    @ApiModelProperty(value = "仓库", example = "1")
    private String warehouse;

    @ApiModelProperty(value = "批次号", example = "898")
    private String batch;

    @ApiModelProperty(value = "生产日期", example = "2023-10-18 18:18:00")
    private LocalDateTime mfd;

    @ApiModelProperty(value = "成本", example = "1.0000")
    private BigDecimal price;

    @ApiModelProperty(value = "数量", example = "1.0000")
    private BigDecimal nums;

    @ApiModelProperty(value = "序列号", example = "1")
    private List<String> serial;

    @ApiModelProperty(value = "总成本", example = "1.0000")
    private BigDecimal total;

    @ApiModelProperty(value = "备注信息", example = "1")
    private String data;
}
