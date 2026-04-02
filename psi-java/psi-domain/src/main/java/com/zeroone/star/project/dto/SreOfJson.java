package com.zeroone.star.project.dto;

import com.zeroone.star.project.dto.j1.homepage.ImyCountsDTO;
import com.zeroone.star.project.dto.j1.homepage.SreCountsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 销售退货单组装json类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("销售退货单")
public class SreOfJson {
    /**
     *  图表类型
     */
    @ApiModelProperty(value = "图标类型",example = "销售退货单")
    String type;
    /**
     每日所有收款的总金额
     */
    @ApiModelProperty(value = "每日所有收款的总金额")
    List<SreCountsDTO> list;
}
