package com.zeroone.star.project.dto.j2.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 18:18
 * @Description: 其他入库单数据对象
 * @Version: 1.0
 */
@Data
@ApiModel("OtherInListDTO")
public class OtherInListDTO {
    @ApiModelProperty(value = "单据ID", example = "1")
    private String id;

    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String frame;

    @ApiModelProperty(value = "供应商", example = "中创集团")
    private String supplier;

    @ApiModelProperty(value = "单据类型", example = "0")
    private Integer type;

    @ApiModelProperty(value = "单据日期", example = "2025-10-16")
    private Long time;

    @ApiModelProperty(value = "单据编号", example = "QTRKD2510161018149")
    private String number;

    @ApiModelProperty(value = "单据成本", example = "140")
    private Double total;

    @ApiModelProperty(value = "单据费用", example = "0")
    private Double cost;

    @ApiModelProperty(value = "关联人员", example = "jack")
    private String people;

    @ApiModelProperty(value = "审核状态", example = "1")
    private Integer examine;

    @ApiModelProperty(value = "费用状态", example = "0")
    private Integer cse;

    @ApiModelProperty(value = "核对状态", example = "0")
    private Integer check;

    @ApiModelProperty(value = "制单人", example = "管理员")
    private String user;

    @ApiModelProperty(value = "备注信息", example = "实验1")
    private String data;
}
