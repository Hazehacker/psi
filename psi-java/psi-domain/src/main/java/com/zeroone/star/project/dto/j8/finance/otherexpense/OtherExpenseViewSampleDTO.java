package com.zeroone.star.project.dto.j8.finance.otherexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 新增其他支出单封装DTO（主表 + 子表）
 * <p>
 * 用于前端一次性传入支出单主表信息与子表明细信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("新增其他支出单封装视图DTO")
public class OtherExpenseViewSampleDTO {

    @ApiModelProperty(value = "主表数据（其他支出单基本信息）", required = true)
    private OtherExpenseViewDTO otherExpense;

    @ApiModelProperty(value = "子表数据列表（其他支出单明细列表）", required = true)
    private List<OtherExpenseInfoViewDTO> otherExpenseInfoList;
}
