package com.zeroone.star.project.dto;

import com.zeroone.star.project.dto.j1.homepage.ImyCountsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 收款单组装json类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("收款单")
public class ImyOfJson {
    /**
     * 图表类型
     */
    @ApiModelProperty(value = "图表类型",example = "收款单")
    String type;
    /**
     每日所有收款的总金额
     */
    @ApiModelProperty(value = "每日所有收款的总金额")
    List<ImyCountsDTO> list;
}

