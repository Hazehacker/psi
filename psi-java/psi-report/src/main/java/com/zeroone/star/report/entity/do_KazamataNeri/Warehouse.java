package com.zeroone.star.report.entity.do_KazamataNeri;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 仓库
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("warehouse")
@ApiModel(value="Warehouse对象", description="仓库")
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "仓库名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "仓库编号")
    @TableField("number")
    private String number;

    @ApiModelProperty(value = "所属组织")
    @TableField("frame")
    private String frame;

    @ApiModelProperty(value = "联系人员")
    @TableField("contacts")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @TableField("tel")
    private String tel;

    @ApiModelProperty(value = "仓库地址")
    @TableField("add")
    private String add;

    @ApiModelProperty(value = "备注信息")
    @TableField("data")
    private String data;


}
