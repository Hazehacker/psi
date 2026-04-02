package com.zeroone.star.project.dto.j4.sale;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("销售单数据对象")
public class SaleImportDTO {
  @ApiModelProperty(value = "关联单据|SOR", example = "1001")
   @ExcelProperty(value = "关联单据|SOR", index = 0)
    private String source;

   @ApiModelProperty(value = "所属组织", example = "2001")
   @ExcelProperty(value = "所属组织", index = 1)
    private String frame;

    @ApiModelProperty(value = "客户", example = "3001")
    @ExcelProperty(value = "客户", index = 2)
    private String customer;
 @ApiModelProperty(value = "单据编号", example = "SELL202504050001", allowableValues = "length <= 32")
 @ExcelProperty(value = "单据编号", index = 3)
 private String number;
    @ApiModelProperty(value = "单据时间", example = "2025-10-18 16:30:00")
    @ExcelProperty(value = "单据时间", index = 4)
    @JsonFormat(pattern = "yyyy-MM-dd ")
    @DateTimeFormat(value = "yyyy/MM/dd")
    private Date time;



    @ApiModelProperty(value = "单据金额", example = "9999.9999")
    @ExcelProperty(value = "单据金额", index = 5)
   // @NumberFormat("#.##")
    private BigDecimal total;

    @ApiModelProperty(value = "实际金额", example = "9800.0000")
    @ExcelProperty(value = "实际金额", index = 6)
  //  @NumberFormat("#.##")
    private BigDecimal actual;

    @ApiModelProperty(value = "实收金额", example = "9800.0000")
    @ExcelProperty(value = "实收金额", index = 7)
   // @NumberFormat("#.##")
    private BigDecimal money;

//    @ApiModelProperty(value = "单据费用", example = "200.0000")
//    @ExcelProperty(value = "单据费用", index = 8)
//    private BigDecimal cost;

    @ApiModelProperty(value = "结算账户", example = "4001")
    @ExcelProperty(value = "结算账户", index = 8)
    private String account;

    @ApiModelProperty(value = "关联人员", example = "5001")
    @ExcelProperty(value = "关联人员", index = 9)
    private String people;

    @ApiModelProperty(value = "物流信息", example = "物流信息")

    @ExcelProperty(value = "物流信息", index = 10)
    private String logistics;

    @ApiModelProperty(value = "备注信息", example = "客户加急订单", allowableValues = "length <= 256")
    @ExcelProperty(value = "备注信息", index = 11)
    private String data;
 @ExcelProperty(value = "制表人", index = 12)
 private String user;
//
//    @ApiModelProperty(value = "扩展信息", example = "扩展信息")
//    @ExcelProperty(value = "扩展信息", index = 13)
//    private String more;
//
//
//
//    @ApiModelProperty(value = "制单人", example = "6001")
//    @ExcelProperty(value = "制单人", index = 14)
//    private String user;

//    @ApiModelProperty(value = "销售详情单")
//   // @ExcelProperty(value = "销售详情单", index = 15)
//    private List<SaleInfoDTO> items;
}
