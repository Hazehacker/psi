package com.zeroone.star.project.dto.j2.store;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-19 13:52
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@ApiModel("CostDTO")
public class CostDTO {
    @ApiModelProperty(value = "所属收支", example = "5")
    private String iet;

    @ApiModelProperty(value = "金额", example = "11.0000")
    private BigDecimal money;

    @ApiModelProperty(value = "备注", example = "1")
    private String data;

    private String id;

    @TableField("class")
    private String clazz;

    private Integer state;

    private String time;

    private String type;

    private BigDecimal settle;
}
