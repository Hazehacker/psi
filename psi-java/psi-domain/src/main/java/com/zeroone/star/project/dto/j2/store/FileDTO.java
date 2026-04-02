package com.zeroone.star.project.dto.j2.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("FileDTO")
public class FileDTO {
    @ApiModelProperty(value = "文件名", example = "aaa.png")
    private String name;
    @ApiModelProperty(value = "url", example = "http:\\/\\/localhost:8010\\/static\\/upload\\/entry\\/69049e3209a8a.png")
    private String url;
}
