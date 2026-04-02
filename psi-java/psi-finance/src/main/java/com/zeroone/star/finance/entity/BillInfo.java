package com.zeroone.star.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;

@Getter
@Setter
@TableName("bill_info")
public class BillInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /*所属ID*/
    private Integer pid;
    /*关联单据*/
    private Integer source;
    /*核销类型*/
    private String bill;
    /*单据类型*/
    private String mold;
    /*核销金额*/
    private Decimal money;
}
