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
 * 待核销单查询参数
 * </p>
 *
 * @author 简单点
 * @since 2025-10-19
 */
@Getter
@Setter
@ApiModel("待核销单查询参数")
public class VerificationPendingBillQuery extends PageQuery {
    //bill+buy+(imy_bill  sell_bill sre_bill  ice_bill)

    //bill
    @ApiModelProperty(value = "单据编号",example = "")
    private String number;

    //buy
    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]",example = "1")
    private Integer nucleus;

    //bill
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "核销开始日期",example = "2020-10-19")
    private String startTime;

    //bill
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "核销截至日期",example = "2025-10-19")
    private String endTime;

    //bill
    @ApiModelProperty(value = "制单人",example = "USER")
    private String user;

    //bill
    @ApiModelProperty(value = "备注信息",example = "无")
    private String data;

    //imy_bill  sell_bill sre_bill   ice_bill
    @ApiModelProperty(value="业务单据类型 [0:收款单|1:销售单|2:销售退货单|3:其他收入单]",example = "1")
    private Integer type2=0;
}
