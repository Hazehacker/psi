package com.zeroone.star.project.query.j5.procurementreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ApiModel(value = "采购付款表单查询参数")
public class PaymentFormQuery extends PageQuery {
    @ApiModelProperty(value = "供应商", example = "工厂")
    private String supplier;
    @ApiModelProperty(value = "单据编号", example = "1000001")
    private String number;
    @ApiModelProperty(value = "开始时间", example = "2025-10-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;
    @ApiModelProperty(value = "结束时间", example = "2025-10-31")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;
    @ApiModelProperty(value = "核销状态", example = "0", allowableValues = "0,1,2")
    private Integer nucleus;
    @ApiModelProperty(value = "单据类型", example = "采购单", allowableValues = "采购单,采购退货单")
    private String type;
}
