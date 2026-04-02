package com.zeroone.star.project.dto.j1.sysconfig;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("字典类型删除数据传输对象")
public class DictTypeDeleteDTO {
    /**
     * 根据id删除字典类型
     */
    @ApiModelProperty(value = "字典类型ID",required = true,  example = "61f3bbc90b904b51890448968afc106f")
    private String id;


}
