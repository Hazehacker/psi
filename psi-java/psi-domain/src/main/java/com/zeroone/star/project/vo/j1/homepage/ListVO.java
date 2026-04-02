package com.zeroone.star.project.vo.j1.homepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "listVO", description = "资金数据封装为list")
public class ListVO<T> {

    @ApiModelProperty(value = "list")
    private List<T> list;
}
