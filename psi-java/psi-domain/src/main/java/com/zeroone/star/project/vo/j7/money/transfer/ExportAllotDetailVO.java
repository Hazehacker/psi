
package com.zeroone.star.project.vo.j7.money.transfer;


import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("转账单详细信息导出模型")
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ExportAllotDetailVO {
    
    @ApiModelProperty(value = "单据日期", example = "2025-10-21")
    @ExcelProperty(value = "单据日期", index = 0)
    private Date time;
   

    @ApiModelProperty(value = "单据编号", example = "DJ2xxx")
    @ExcelProperty(value = "单据编号", index = 1)
    private String number;
    
    @ApiModelProperty(value = "转出账户名称", example = "转出账户")
    @ExcelProperty(value = "转出账户", index = 2)
    private String outName;
    

    @ApiModelProperty(value = "转入账户名称", example = "转入账户")
    @ExcelProperty(value = "转入账户", index = 3)
    private String inName;
    

    @ApiModelProperty(value = "结算金额", example = "100.00")
    @ExcelProperty(value = "结算金额", index = 4)
    private BigDecimal money;
   

    @ApiModelProperty(value = "结算号", example = "JS2xxx5")
    @ExcelProperty(value = "结算号", index = 5)
    private String settle;


   
    @ApiModelProperty(value = "备注信息", example = "月度内部转账")
    @ExcelProperty(value = "备注信息", index = 6)
    private String data;

  
    @ApiModelProperty(value = "单据金额", example = "120.00")
    @ExcelProperty(value = "单据金额", index = 7)
    private BigDecimal total;
   

    @ApiModelProperty(value = "关联人员名称", example = "王五")
    @ExcelProperty(value = "关联人员名称", index = 8)
    private String peopleName;

    @ApiModelProperty(value = "备注信息", example = "信息")
    @ExcelProperty(value = "详细备注信息", index = 9)
    private String remark;

}
