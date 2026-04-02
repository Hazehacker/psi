package com.zeroone.star.project.dto.j8.finance.otherincome;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("其他收入单导入数据返回对象")
public class OtherIncomeImportResultDTO {

    @ApiModelProperty(value = "导入的数据的总条数",example = "6")
    private int totalCount;

    @ApiModelProperty(value = "导入的数据成功的条数的条数",example = "5")
    private int successCount;

    @ApiModelProperty(value = "导入的数据失败的条数的条数",example = "1")
    private int failCount;

    @ApiModelProperty(value = "导入的数据失败的详细信息",example = "缺少支出单的时间")
    private List<String> failDetails;
}