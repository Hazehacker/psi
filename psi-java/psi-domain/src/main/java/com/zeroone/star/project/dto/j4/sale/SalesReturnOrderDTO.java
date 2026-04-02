package com.zeroone.star.project.dto.j4.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.zeroone.star.project.dto.j4.sale.info.SalesReturnOrderInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("销售退货单数据对象")
public class SalesReturnOrderDTO {

    @ApiModelProperty(value = "销售退货单id",example = "1")
    private String id;
    @ApiModelProperty(value = "所属销售单id",example = "0")
    private String source;
    @ApiModelProperty(value = "客户id",example = "1")
    private String customer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "单据时间",example = "2023-10-25 14:30:00")
    private LocalDateTime time;
    @ApiModelProperty(value = "单据编号",example = "123456789")
    private String number;
    @ApiModelProperty(value = "单据金额",example = "5.0")
    private BigDecimal total;
    @ApiModelProperty(value = "实际金额",example = "5.0")
    private BigDecimal actual;
    @ApiModelProperty(value = "核对状态:0未核对，1已核对",example = "0")
    private Integer check;
    @ApiModelProperty(value = "单据费用",example = "1")
    private BigDecimal cost;
    @ApiModelProperty(value = "审核状态:0未审核，1已审核",example = "0")
    private Integer examine;
    @ApiModelProperty(value = "核销状态:0未核销，1部分核销，2已核销",example = "0")
    private Integer nucleus;
    @ApiModelProperty(value = "费用状态：0-未结算，1-部分结算，2-已结算，3-无需结算", example = "2")
    private Integer cse;
    @ApiModelProperty(value = "发票状态：0-未开票，1-部分开票，2-已开票，3-无需开具", example = "3")
    private Integer invoice;
    @ApiModelProperty(value = "制单人ID（关联is_user表）", example = "1")
    private String user;
    @ApiModelProperty(value = "备注",example = "无")
    private String data;
    @ApiModelProperty(value = "实付金额",example = "5.0")
    private BigDecimal money;
    @ApiModelProperty(value = "关联人员id",example = "1")
    private String people;
    @ApiModelProperty(value = "销售退货单详情数据对象",example = "")
    private SalesReturnOrderInfo[] salesReturnOrderInfos;
}
