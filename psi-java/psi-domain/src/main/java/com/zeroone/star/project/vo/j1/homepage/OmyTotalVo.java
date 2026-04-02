package com.zeroone.star.project.vo.j1.homepage;

import com.zeroone.star.project.dto.j1.homepage.OmyDailyTotalDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel(description = "付款单响应对象")
public class OmyTotalVo {
    @ApiModelProperty(notes = "类型",example = "付款单")
    String type;
    @ApiModelProperty(notes = "数据项列表")
    List<OmyDailyTotalDto> list;
}
