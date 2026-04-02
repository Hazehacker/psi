package com.zeroone.star.finance.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /*供应商*/
    private String supplier;
    /*客户*/
    private String customer;
    /*单据编号*/
    private String number;
    /*单据时间*/
    private LocalDateTime time;
    /*核销类型*/
    private Integer type;
    /*总核金额*/
    private Decimal pmy;
    /*总销金额*/
    private Decimal smp;
    /*关联人员*/
    private String people;
    /*单据附件*/
    private String file;
    /*备注信息*/
    private String date;
    /*扩展信息*/
    private Long more;
    /*审核状态*/
    private Integer examine;
    /*制单人*/
    private String user;



}
