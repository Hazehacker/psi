package com.zeroone.star.project.query.j3.capital;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * <p>
 * 核销单查询参数
 * </p>
 *
 * @author 简单点
 * @since 2025-10-19
 */
@Getter
@Setter
@ApiModel("核销单查询参数")
public class VerificationBillWrittenQuery extends PageQuery {
    //bill + bill_info

    @ApiModelProperty(value = "客户",example = "CUST")
    private String customer;

    @ApiModelProperty(value = "供应商",example = "SUPP")
    private String supplier;

    @ApiModelProperty(value = "单据编号",example = "BILL")
    private String number;

    @ApiModelProperty(value = "核销类型 [0:预收冲应收|1:预付冲应付|2:应收冲应付|3:销退冲销售|4:购退冲采购]",example = "2")
    private Integer type;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "核销单开始日期",example = "2020-10-19")
    private String startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "核销单截至日期",example = "2025-10-19")
    private String endTime;

    @ApiModelProperty(value = "制单人",example = "USER")
    private String user;

    @ApiModelProperty(value = "关联人员",example = "")
    private String people;

    @ApiModelProperty(value = "审核状态 [0:未审核|1:已审核]",example = "0")
    private Integer examine;

    @ApiModelProperty(value = "备注信息",example = "")
    private String data;
}
