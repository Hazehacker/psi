package com.zeroone.star.project.dto.j5.fundreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("供应商对账表-主单据数据对象")
public class SupplierStatementMainDTO {

    @ApiModelProperty(value = "单据类型",example = "销售单")
    private String tableType;

    @ApiModelProperty(value = "所属组织",example = "公司")
    private String frame;

    @ApiModelProperty(value = "单据时间",example = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号",example = "1")
    private String number;

    @ApiModelProperty(value = "单据金额",example = "123456789012.1234")
    private BigDecimal total;

    @ApiModelProperty(value = "优惠金额",example = "123456789012.1234")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "应付金额",example = "123456789012.1234")
    private BigDecimal actual;

    @ApiModelProperty(value = "实付金额",example = "123456789012.1234")
    private BigDecimal money;

    @ApiModelProperty(value = "应付款金额",example = "123456789012.1234")
    private BigDecimal actual2;

    //    以下两个是同时要有的
    @ApiModelProperty(value = "明细列表",example = "看项目计划图")
    private List<SupplierStatementDetailDTO> details;

    @ApiModelProperty(value = "前端折叠控制标识，默认折叠状态")
    private boolean expanded =false;

    @ApiModelProperty(value = "备注",example = "你好")
    private String data;

}
