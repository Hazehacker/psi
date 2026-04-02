package com.zeroone.star.project.dto.j7.money.transfer;

import cn.hutool.core.date.DateTime;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 类名：ImportAllotDTO
 * 包名：com.zeroone.star.project.dto.j7.money.transfer
 * 描述：导入数据传递参数封装类
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@Data
@ApiModel("转账单导入模型")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportAllotDTO {
    @ApiModelProperty(value = "单据日期", example = "2025-10-21")
    @ExcelProperty(value = "单据日期", index = 0)
    private DateTime time;

    @ApiModelProperty(value = "单据编号", example = "DJ2025xxx")
    @ExcelProperty(value = "单据编号", index = 1)
    private String number;

    @ApiModelProperty(value = "单据金额", example = "100.00")
    @ExcelProperty(value = "单据金额", index = 2)
    private BigDecimal total;

    @ApiModelProperty(value = "关联人员名称", example = "赵六")
    @ExcelProperty(value = "关联人员", index = 3)
    private String peopleName;

    @ApiModelProperty(value = "备注信息", example = "跨部门资金周转")
    @ExcelProperty(value = "备注信息", index = 4)
    private String data;

    @ApiModelProperty(value = "转出账户名称", example = "销售部主账户")
    @ExcelProperty(value = "转出账户名称", index = 5)
    private String outName;

    @ApiModelProperty(value = "转入账户名称", example = "财务部专用账户")
    @ExcelProperty(value = "转入账户名称", index = 6)
    private String inName;

    @ApiModelProperty(value = "结算余额", example = "100.00")
    @ExcelProperty(value = "结算余额", index = 7)
    private BigDecimal money;
    @ApiModelProperty(value = "结算号", example = "JS2xxx")
    @ExcelProperty(value = "结算号", index = 8)
    private String settle;

    @ApiModelProperty(value = "备注信息", example = "信息")
    @ExcelProperty(value = "详细备注信息", index = 9)
    private String remark;
}