package com.zeroone.star.project.query.j2.store;

import com.zeroone.star.project.dto.j2.store.CostDTO;
import com.zeroone.star.project.dto.j2.store.OtherInListInfoDTO;
import com.zeroone.star.project.dto.j2.store.OtherOutListInfoDTO;
import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.query.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 18:12
 * @Description: 其他出库单参数对象
 * @Version: 1.0
 */
@Data
@ApiModel("OtherOutQuery")
public class OtherOutQuery extends PageQuery {
    @ApiModelProperty(value = "id", example = "1")
    private String id;
    @ApiModelProperty(value = "商品名称", example = "A4纸")
    private String name;
    @ApiModelProperty(value = "供应商", example = "1")
    private String customer;
    @ApiModelProperty(value = "所属组织", example = "0")
    private String frame;
    @ApiModelProperty(value = "单据时间", example = "1695043200")
    private String time;
    @ApiModelProperty(value = "单据编号", example = "OT202310190001")
    private String number;
    @ApiModelProperty(value = "单据类型[0:其它出库单|1:盘亏单]", example = "0")
    private Integer type;
    @ApiModelProperty(value = "单据成本", example = "1000.00")
    private BigDecimal total;
    @ApiModelProperty(value = "单据费用", example = "50.00")
    private BigDecimal cost;
    @ApiModelProperty(value = "开始日期", example = "2024-10-19")
    private String startTime;
    @ApiModelProperty(value = "结束日期", example = "2025-10-19")
    private String endTime;
    @ApiModelProperty(value = "关联人员", example = "1")
    private Integer people;
    @ApiModelProperty(value = "物流信息", example = "顺丰快递")
    private String logistics;
    @ApiModelProperty(value = "单据附件", example = "file.pdf")
    private String file;
    @ApiModelProperty(value = "备注信息", example = "备注")
    private String data;
    @ApiModelProperty(value = "扩展信息", example = "{}")
    private String more;
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "0")
    private Integer examine;
    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]", example = "0")
    private Integer cse;
    @ApiModelProperty(value = "核对状态[0:未核对|1:已核对]", example = "0")
    private Integer check;
    @ApiModelProperty(value = "制单人", example = "1")
    private Integer user;

}
