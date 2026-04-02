package com.zeroone.star.project.query.j1.sysconfig;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 描述：字典类型查询数据对象
 * </p>
 *
 * @Author: ZGjie20
 * @version: 1.0.0
 */
@Data
@ApiModel("字典类型查询数据对象")

public class DictTypeQury extends PageQuery {
    @ApiModelProperty(value = "字典类型id"  ,example = "61f3bbc90b904b51890448968afc106f")
    String id;
    @ApiModelProperty(value = "字典类型名称"  ,example = "文件类型")
    String name;
    @ApiModelProperty(value = "字典类型编码"  ,example = "file_type")
    String code;
    @ApiModelProperty(value = "字典类型备注"  ,example = "备注")
    String remark;

}
