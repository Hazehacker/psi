package com.zeroone.star.project.dto.j3.capital;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 核销单DTO
 * </p>
 *
 * @author 简单点
 * @since 2025-10-19
 */
@Data
@ApiModel("核销单DTO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationSheetBillWrittenDTO {
    //bill + bill_info
    // 11 个字段
    @ApiModelProperty(value = "所属组织",example = "暂无")
    private String frame;

    @ApiModelProperty(value = "客户",example = "张三")
    private String customer;

    @ApiModelProperty(value = "供应商",example = "李四")
    private String supplier;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "单据时间",example = "2025-10-19")
    private LocalDate time;

    @ApiModelProperty(value = "单据编号",example = "SKD2509231616598")
    private String number;

    //bill_info
    @ApiModelProperty(value = "核销类型 [0:预收冲应收|1:预付冲应付|2:应收冲应付|3:销退冲销售|4:购退冲采购]",example = "预收冲应收")
    private String bill;

    // bill_info
    @ApiModelProperty(value="核销金额",example = "116.09")
    private BigDecimal money;

    @ApiModelProperty(value = "关联人员",example = "崔瀚帅")
    private String people;

    @ApiModelProperty(value = "审核状态 [0:未审核|1:已审核]",example = "0",notes = "查询到的为int类型，需要手动转换成对应的string字符串")
    private String examine;

    @ApiModelProperty(value = "制单人",example = "管理员")
    private String user;

    @ApiModelProperty(value = "备注信息",example = "无")
    private String data;



    public void setExamine(Integer examine) {
        this.examine = convertExamine(examine);
    }

    public String convertExamine(Integer examine) {
        if(examine==null){
            return "未知";
        }
        switch (examine){
            case 0:{
                return "未审核";
            }
            case 1:{
                return "已审核";
            }
            default:
                return  "未知";
        }
    }
}
