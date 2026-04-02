
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

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportAllotVO {
  
    @ApiModelProperty(value = "所属组织", example = "所属组织")
    @ExcelProperty(value = "所属组织", index = 0)
    private String frameName;
  

    @ApiModelProperty(value = "单据时间", example = "2025-10-21")
    @ExcelProperty(value = "单据时间", index = 1)
    private Date time;
   

    @ApiModelProperty(value = "单据编号", example = "DJ20xxx")
    @ExcelProperty(value = "单据编号", index = 2)
    private String number;
    

    @ApiModelProperty(value = "单据金额", example = "1890.50")
    @ExcelProperty(value = "单据金额", index = 3)
    private BigDecimal total;
   

    @ApiModelProperty(value = "关联人员名称", example = "张三")
    @ExcelProperty(value = "关联人员名称", index = 4)
    private String peopleName;
    

    @ApiModelProperty(value = "审核状态 0:未审核 1:已审核", example = "0")
    @ExcelProperty(value = "审核状态", index = 5)
    private Integer examine;
    

    @ApiModelProperty(value = "制单人", example = "李四")
    @ExcelProperty(value = "制单人", index = 6)
    private String userName;
    

    @ApiModelProperty(value = "备注信息", example = "信息")
    @ExcelProperty(value = "备注信息", index = 7)
    private String data;

    
    @ApiModelProperty(value = "总数", example = "20")
    @ExcelProperty(value = "总数", index = 8)
    private Integer count;
   

    @ApiModelProperty(value = "总金额", example = "100.00")
    @ExcelProperty(value = "总金额", index = 9)
    private BigDecimal sumMoney;
}
