package com.zeroone.star.project.vo.j6.basic_information.supplier_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("导入结果VO")
public class ImportResultVO {


    @ApiModelProperty("总记录数")
    private Integer totalCount;

    @ApiModelProperty("成功数量")
    private Integer successCount;

    @ApiModelProperty("失败数量")
    private Integer failureCount;

    @ApiModelProperty("失败详情列表")
    private List<ImportFailureVO> failureDetails;

    @ApiModelProperty("导入文件下载地址")
    private String resultFileUrl;
}
