package com.zeroone.star.project.dto.j8.finance.otherincome;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("其他收入单审核/反审核")
public class OtherIncomeCheckOrUncheckDTO {

    //加入一个ice表的id，也就是传递一个id给我，并且传递

    @ExcelProperty("其他收入单编号")
    @ApiModelProperty(value = "id",example = "1")
    private String id;

    @ExcelProperty("审核状态")
    @ApiModelProperty(value = "审核状态",example = "1")
    private Integer examine;




}
