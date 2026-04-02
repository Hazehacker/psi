package com.zeroone.star.project.query.j5.salesreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

/**
 * 销售单查询类
 * @author 言语
 * @date 2025/10/20
 */
//is_sell
@Data
@ApiModel("销售单表数据对象")
public class SalesQuery extends PageQuery {

    @ApiModelProperty(value = "客户" ,example = "CUST001")
    private String customer;
    @ApiModelProperty(value = "单据编号", example = "SALE202510001")
    private String number;
    @ApiModelProperty(value = "单据开始时间",example = "2025-10-01")
    private String beginTime;
    @ApiModelProperty(value = "单据结束时间",example = "2025-10-01")
    private String leftTime;
    @ApiModelProperty(value = "制单人",example = "USER001")
    private String user;
    @ApiModelProperty(value = "关联人员",example = "EMP001")
    private String people;
    @ApiModelProperty(value = "单据类型",example = "销售单")
    private String type;
    @ApiModelProperty(value = "明细" , example = "隐藏明细",required = true)
    private String data;

}
