package com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 *描述 ：导出时的excel类
 *Author:kunge
 */
@ApiModel("导出条码响应对象")
@Data
public class ExportCodeDTO {

    @ApiModelProperty(value = "条码名称",example = "衣服")
    @ExcelProperty(value = "条码名称",index = 0)
    private String name;

    @ApiModelProperty(value = "条码内容",example = "xxx")
    @ExcelProperty(value ="条码内容",index = 1)
    private String info;

    @ApiModelProperty(value = "条码类型",example = "二维码")
    @ExcelProperty(value = "条码类型",index = 2)
//    0条形码 1二维码
    private String type;

    @ApiModelProperty(value = "条码图片",example = "图片")
    @ExcelProperty(value = "条码图片",index = 3)
    private byte[] img;

    @ApiModelProperty(value = "备注信息",example = "测试")
    @ExcelProperty(value = "备注信息",index = 4)
    private String data;

}
