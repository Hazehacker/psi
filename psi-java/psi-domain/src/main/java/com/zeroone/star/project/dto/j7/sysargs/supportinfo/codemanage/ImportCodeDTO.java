package com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/*
 *描述 ：导入时的excel类
 *Author:kunge
 */
@Data
@ApiModel("导入条码响应对象")
public class ImportCodeDTO {

    @ExcelProperty(value = "条码名称",index = 0)
    private String name;

    @ExcelProperty(value ="条码内容",index = 1)
    private String info;

    @ExcelProperty(value = "条码类型",index = 2)
//    0条形码 1二维码
    private String type;

    @ExcelProperty(value = "备注信息",index = 3)
    private String data;

}
