package com.zeroone.star.project.query.j7.sysargs.supportinfo.attachment;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation("附件查询参数")
public class AnnexQuery extends PageQuery {
    @ApiModelProperty(value = "文件名")
    private String name;
    @ApiModelProperty(value = "文件类型,对应字典表value")
    private String fileType;
    @ApiModelProperty(value = "文件存储方式,对应字典表value")
    private String saveType;
    @ApiModelProperty(value = "文件存储路径:不要包含服务器域名和端口")
    private String savePath;
    @ApiModelProperty(value = "文件状态,0未使用，1使用中")
    private Integer status;
}
