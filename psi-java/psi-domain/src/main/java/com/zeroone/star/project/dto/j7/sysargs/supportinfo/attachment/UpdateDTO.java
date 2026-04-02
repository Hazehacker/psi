package com.zeroone.star.project.dto.j7.sysargs.supportinfo.attachment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 类名：UpdateDTO
 * 包名：com.zeroone.star.project.dto.j7.sysargs.supportinfo.attachment
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/22
 * 版本号：V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("更新附件响应对象")
public class UpdateDTO implements Serializable {


    protected static final long serialVersionUID = 1L;

    /**
     * 附件唯一标识
     */
    @NotNull(message = "附件ID不能为空")
    @ApiModelProperty(value = "附件唯一标识", example = "123123")
    protected String id;

    /**
     * 附件名称
     */
    @ApiModelProperty(value = "附件名称", example = "File.pdf")
    protected String fileName;

    /**
     * 附件类型
     */
    @ApiModelProperty(value = "附件类型", example = ".pdf")
    protected String fileType;

    /**
     * 附件大小
     */
    @ApiModelProperty(value = "附件大小", example = "1024")
    protected Long fileSize;

    /**
     * 附件路径
     */
    @ApiModelProperty(value = "附件路径", example = "/uploads/2025/10/file.pdf")
    protected String fileUrl;

    /**
     * 存储分组名
     */
    @ApiModelProperty(value = "存储分组名", example = "j7")
    protected String groupName;

    /**
     * 上传人
     */
    @ApiModelProperty(value = "上传人", example = "noy")
    protected String uploadUser;

    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间", example = "2025-10-19T12:00:00")
    protected LocalDateTime uploadTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "比如说我这次修改过了")
    protected String remark;
}
