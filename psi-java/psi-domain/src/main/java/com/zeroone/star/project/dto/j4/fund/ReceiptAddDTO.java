package com.zeroone.star.project.dto.j4.fund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("新增有款单")
public class ReceiptAddDTO {
    @ApiModelProperty(value = "客户id",example = "1")
    private String customer;
    
    @ApiModelProperty(value = "单据日期", example = "2025-10-19")
    private LocalDateTime time;
    
    @ApiModelProperty(value = "单据编号",example = "123456789")
    private String number;
    
    @ApiModelProperty(value = "单据金额",example = "36.0")
    private BigDecimal total;
    
    @ApiModelProperty(value = "关联人员",example = "1")
    private String people;
    
    @ApiModelProperty(value = "单据附件",example = "1")
    private List<String> file;
    
    @ApiModelProperty(value = "备注信息",example = "1")
    private String data;

    @ApiModelProperty(value = "扩展信息",example = "1")
    private String more;
    
    @ApiModelProperty(value = "收款单结算明细")
    private List<ReceiptSettlementDetailDTO> info;
    
    @ApiModelProperty(value = "创建人id",example = "1")
    private String user;
    
    @ApiModelProperty(value = "所属组织", example = "0")
    private String frame;
}