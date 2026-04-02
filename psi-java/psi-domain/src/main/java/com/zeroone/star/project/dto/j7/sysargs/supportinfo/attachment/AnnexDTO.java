package com.zeroone.star.project.dto.j7.sysargs.supportinfo.attachment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@ApiModel("附件信息")
public class AnnexDTO {

    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "文件唯一标识", example = "f888f8a0-7d2b-4c1a-9e3f-2d1c3e4b5a6f")
    @NotBlank(message = "附件id不能为空")
    private String id;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称（包含扩展名）", example = "项目需求文档v2.0.docx")
    private String name;

    /**
     * 文件类型，值对应字典表value
     */
    @ApiModelProperty(value = "文件类型（对应字典表value，如doc、pdf、jpg等）", example = "docx")
    private String fileType;

    /**
     * 文件存储方式，值对应字典表value
     */
    @ApiModelProperty(value = "文件存储方式（对应字典表value，如local-本地存储、oss-对象存储等）", example = "oss")
    private String saveType;

    /**
     * 文件存储路径，不要将服务器域名和端口存储到数据库
     */
    @ApiModelProperty(value = "文件存储路径（不含服务器域名和端口）", example = "/upload/files/202510/project/")
    private String savePath;

    /**
     * 备注
     */
    @ApiModelProperty(value = "文件备注信息", example = "用于项目立项评审的需求文档")
    private String remark;

    /**
     * 数据状态 0 未使用 1使用中
     */
    @ApiModelProperty(value = "数据状态（0-未使用，1-使用中）", example = "1")
    private Integer status;
}
