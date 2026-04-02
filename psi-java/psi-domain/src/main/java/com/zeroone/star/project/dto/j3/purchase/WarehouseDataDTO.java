package com.zeroone.star.project.dto.j3.purchase;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "仓库基础信息（数据库warehouse表字段）")
public class WarehouseDataDTO {
    @ApiModelProperty(value = "仓库ID", example = "1")
    private String id;
    @ApiModelProperty(value = "仓库名称", example = "1号仓库")
    private String name;
    @ApiModelProperty(value = "仓库编号", example = "CG0001")
    private String number;
    @ApiModelProperty(value = "所属组织ID", example = "1")
    private String frame;
    @ApiModelProperty(value = "仓库联系人", example = "")
    private String contacts;
    @ApiModelProperty(value = "仓库电话", example = "")
    private String tel;
    @ApiModelProperty(value = "仓库地址", example = "")
    @TableField("`add`")
    private String add;
    @ApiModelProperty(value = "仓库备注", example = "")
    private String data;
}