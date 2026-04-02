package com.zeroone.star.project.query.j2.store;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.query.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 18:11
 * @Description: 其他入库单参数对象
 * @Version: 1.0
 */
@Data
@ApiModel("OtherInQuery")
public class OtherInQuery extends PageQuery {
    @ApiModelProperty(value = "商品名称", example = "A4纸")
    private String name;

    @ApiModelProperty(value = "单据编号", example = "QTRKD2510161018149")
    private String number;

    @ApiModelProperty(value = "供应商", example = "中创集团")
    private String supplier;

    @ApiModelProperty(value = "制单人", example = "管理员")
    private String user;

    @ApiModelProperty(value = "开始日期", example = "2024-10-19")
    private String startTime;

    @ApiModelProperty(value = "结束日期", example = "2025-10-19")
    private String endTime;

    @ApiModelProperty(value = "关联人员", example = "jack")
    private String people;

    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核|-1:全部]", example = "1")
    private Integer examine;

    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算|-1:全部]", example = "0")
    private Integer cse;

    @ApiModelProperty(value = "核对状态[0:未核对|1:已核对|-1:全部]", example = "0")
    private Integer check;

    @ApiModelProperty(value = "单据类型[0:其他入库单|1:盘盈单]", example = "0")
    private Integer type;

    @ApiModelProperty(value = "备注信息", example = "实验1")
    private String data;

    @ApiModelProperty(value = "所属组织", example = "0")
    private String frame;
}
