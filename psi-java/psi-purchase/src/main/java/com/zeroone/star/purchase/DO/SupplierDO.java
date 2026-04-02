package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author 唐怀瑟
 * @Data 2025/10/24 23:10
 * @PackageName: com.zeroone.star.purchase.DO
 * @CLASSNAME: supplierDO
 * @Description: 供应商
 * @Version 1.0
 */
@Getter
@Setter
@TableName("supplier")
public class SupplierDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 拼音信息
     */
    private String py;

    /**
     * 供应商编号
     */
    private String number;

    /**
     * 所属组织
     */
    private String frame = "0";

    /**
     * 所属用户
     */
    private String user;

    /**
     * 供应商类别
     */
    private String category;

    /**
     * 增值税税率
     */
    private BigDecimal rate = BigDecimal.ZERO;

    /**
     * 开户银行
     */
    private String bank;

    /**
     * 银行账号
     */
    private String account;

    /**
     * 纳税号码
     */
    private String tax;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 联系资料
     */
    private String contacts;

    /**
     * 应付款余额
     */
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * 扩展信息
     */
    private String more;

}
