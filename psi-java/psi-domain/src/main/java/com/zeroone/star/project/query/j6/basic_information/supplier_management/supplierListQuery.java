package com.zeroone.star.project.query.j6.basic_information.supplier_management;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 描述：供应商查询参数
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */

@Data
@ApiModel("供应商查询参数")
public class supplierListQuery extends PageQuery {

    @ApiModelProperty(value = "供应商名称", example = "供应商名称")
    private String name;

    @ApiModelProperty(value = "供应商编号", example = "123456")
    private String number;

    @ApiModelProperty(value = "供应商类别", example = "常规类别")
    private String category;

    @ApiModelProperty(value = "所属组织ID", example = "1")
    private Integer frame;

    @ApiModelProperty(value = "所属用户ID", example = "1")
    private Integer user;

    @ApiModelProperty(value = "备注信息", example = "任意备注")
    private String data;

    @ApiModelProperty(value = "应付金额最小值", example = "0.0")
    private Double minBalance;

    @ApiModelProperty(value = "应付金额最大值", example = "10000.0")
    private Double maxBalance;

    @ApiModelProperty(value = "联系人姓名", example = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系人电话", example = "12345678910")
    private String phone;
}
