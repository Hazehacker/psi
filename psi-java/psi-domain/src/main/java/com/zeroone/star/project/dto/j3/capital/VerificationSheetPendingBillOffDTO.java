package com.zeroone.star.project.dto.j3.capital;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.*;

import org.apache.poi.hssf.record.crypto.Biff8DecryptingStream;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 待核销单DTO
 * </p>
 *
 * @author 简单点
 * @since 2025-10-19
 */
@Data
@SuppressWarnings("all")
@ApiModel("待核销单DTO")
@AllArgsConstructor
@NoArgsConstructor
public class VerificationSheetPendingBillOffDTO {
    //该类的字段对应数据库表 bill 和 buy

    //bill
    @ApiModelProperty(value = "所属组织",example="暂无")
    private String frame;

    //bill
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "单据时间",example = "2025-10-19")
    private LocalDate time;

    //bill
    @ApiModelProperty(value = "单据编号",example = "SKD2509231616598")
    private String number;

    //buy
    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]",example = "0")
    private String nucleus;

    //buy
    @ApiModelProperty(value = "单据金额",example = "1000.00")
    private BigDecimal total;

    //imy_bill sell_bill sre_bill ice_bill
    @ApiModelProperty(value = "已核销金额",example = "500.00")
    private BigDecimal money;

    //单据金额-核销金额（核销金额分别在 imy_bill sell_bill sre_bill ice_bill）
    @ApiModelProperty(value = "未核销金额",example = "500.00")
    private BigDecimal diffMoney;

    //bill
    @ApiModelProperty(value = "关联人员",example = "管理员")
    private String people;

    //bill
    @ApiModelProperty(value = "制单人",example = "张瀚帅")
    private String user;

    //bill
    @ApiModelProperty(value = "备注信息",example = " ")
    private String data;


    public String getNucleus() {
        return nucleus;
    }

    public void setNucleus(String nucleus) {
        this.nucleus =convertNucleus(nucleus);
    }

    private String convertNucleus(String nucleus){
        if(nucleus==null){
            return "未知";
        }
        switch (nucleus){
            case "0":
                return "未核销";
            case "1":
                return "部分核销";
            case "2":
                return "已核销";
            default:
                return "未知";
        }
    }

    public void setDiffMoney() {
        if(this.total!=null && this.money!=null){
            this.diffMoney=this.total.subtract(this.money);
        }else{
            this.diffMoney=BigDecimal.ZERO;
        }
    }
}



