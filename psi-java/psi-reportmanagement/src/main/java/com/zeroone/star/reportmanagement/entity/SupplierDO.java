package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 供应商
 * @author 天天困
 * @date 2025/10/23
 */
@Getter
@Setter
@TableName("supplier")
public class SupplierDO {

    /**
     * 唯一id（主键）
     */
    @TableId(value = "id", type = IdType.NONE)
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
    private String frame;

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
    private BigDecimal rate;

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
    private BigDecimal balance;

    /**
     * 扩展信息
     */
    private String more;
}
