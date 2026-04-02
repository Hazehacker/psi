package com.zeroone.star.project.dto.j4.sale;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("销售单导出数据对象Dto")
public class SaleExportDTO {
    //销售单列表导出
//    @ApiModelProperty(value = "主键ID", example = "1")
//    private String id;

//    @ApiModelProperty(value = "关联单据|SOR", example = "1001")
//    private String source;

    @ApiModelProperty(value = "所属组织", example = "2001")
    @ExcelProperty(value = "所属组织", index = 0)
    private String frame;

    @ApiModelProperty(value = "客户", example = "3001")
    @ExcelProperty(value = "客户", index = 1)
    private String customer;

    @ApiModelProperty(value = "单据时间", example = "2025-10-18 ")
    @ExcelProperty(value = "单据时间", index = 2)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号", example = "SELL202504050001", allowableValues = "length <= 32")
    @ExcelProperty(value = "单据编号", index = 3)
    private String number;

    @ApiModelProperty(value = "单据金额", example = "9999.9999")
    @ExcelProperty(value = "单据金额", index = 4)
    private BigDecimal total;

    @ApiModelProperty(value = "实际金额", example = "9800.0000")
    @ExcelProperty(value = "实际金额", index = 5)
    private BigDecimal actual;

//    @ApiModelProperty(value = "实收金额", example = "9800.0000")
//    private BigDecimal money;

    @ApiModelProperty(value = "单据费用", example = "200.0000")
    @ExcelProperty(value = "单据费用", index = 6)
    private BigDecimal cost;
    @ApiModelProperty(value = "关联人员", example = "5001")
    @ExcelProperty(value = "关联人员", index = 7)
    private String people;
    @ApiModelProperty(value = "结算账户", example = "4001")
    @ExcelProperty(value = "结算账户", index = 8)
    private String account;



//    @ApiModelProperty(value = "物流信息", example = "物流信息")
//    private String logistics;

//    @ApiModelProperty(value = "单据附件", example = "/upload/file1.pdf,/upload/img1.png")
//    private String file;



    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "0")
    @ExcelProperty(value = "审核状态[0:未审核|1:已审核]", index = 9)
    private Integer examine;

    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]", example = "0")
    @ExcelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]", index = 10)
    private Integer nucleus;

    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]", example = "0")
    @ExcelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]", index = 11)
    private Integer cse;

    @ApiModelProperty(value = "发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]", example = "0")
    @ExcelProperty(value = "发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]", index = 12)
    private Integer invoice;

    @ApiModelProperty(value = "核对状态[0:未核对|1:已核对]", example = "0")
    @ExcelProperty(value = "核对状态[0:未核对|1:已核对]", index = 13)
    private Integer check;

    @ApiModelProperty(value = "制单人", example = "6001")
    @ExcelProperty(value = "制单人", index = 14)
    private String user;

    @ApiModelProperty(value = "备注信息", example = "客户加急订单", allowableValues = "length <= 256")
    @ExcelProperty(value = "备注信息", index = 15)
    private String data;

}
