package com.zeroone.star.project.dto.j8.finance.tradeexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.time.LocalDateTime;

@Data
@ApiModel("购销费用数据对象")
public class BillDTO {
    @ApiModelProperty(value = "id",example="1")
    private Integer id;
    @ApiModelProperty(value = "供应商",example = "中创集团")
    private String supplier;
    @ApiModelProperty(value = "客户",example = "理想")
    private String customer;
    @ApiModelProperty(value = "单据编号",example = "XSD2506131730194\n")
    private String number;
    @ApiModelProperty(value = "单据时间",example = "2024-10-22")
    private LocalDateTime time;
    @ApiModelProperty(value = "核销类型",example = "0")
    private Integer type;
    @ApiModelProperty(value = "总核金额",example = "10")
    private Decimal pmy;
    @ApiModelProperty(value = "总销金额",example = "10")
    private Decimal smp;
    @ApiModelProperty(value = "关联人员",example = "四川张三")
    private String people;
    @ApiModelProperty(value = "单据附件",example = "xxx")
    private String file;
    @ApiModelProperty(value = "备注信息",example = "xxx")
    private String date;
    @ApiModelProperty(value = "扩展信息",example = "xxx")
    private Long more;
    @ApiModelProperty(value = "审核状态",example = "1")
    private Integer examine;
    @ApiModelProperty(value = "制单人",example = "xxx")
    private String user;



}
