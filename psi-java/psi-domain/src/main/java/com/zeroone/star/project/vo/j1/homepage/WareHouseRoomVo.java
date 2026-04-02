package com.zeroone.star.project.vo.j1.homepage;

import com.zeroone.star.project.dto.j1.homepage.WareHouseRoomDataDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel(description = "库存数据响应对象")
public class WareHouseRoomVo {
    @ApiModelProperty(notes = "数据项列表")
    List<WareHouseRoomDataDto> list;
}
