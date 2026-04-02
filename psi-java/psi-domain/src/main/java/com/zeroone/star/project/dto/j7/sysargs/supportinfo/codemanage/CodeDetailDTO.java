package com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 类名：CodeDetailDTO
 * 包名：com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage
 * 描述：条码详情响应数据对象
 * 作者：star
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@Data
@ApiModel("条码详情响应数据对象")
public class CodeDetailDTO {
    @ApiModelProperty(value = "条码ID", example = "CODE20251018001")
    private String id;

    @ApiModelProperty(value = "条码名称", example = "产品QR码")
    private String name;

    @ApiModelProperty(value = "条码内容", example = "https://www.example.com/product/123")
    private String info;

    @ApiModelProperty(value = "条码类型", notes = "0:条形码, 1:二维码", example = "1")
    private Integer type;

    @ApiModelProperty(value = "备注信息", example = "产品信息二维码")
    private String data;
}