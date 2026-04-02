package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户
 * @author rainsilent
 * @date 2025/10/22
 */
@Getter
@Setter
@TableName("customer")
public class CustomerDO {
    /**
     * 唯一id
     */
    @TableId(value = "id")
    private String id;
    /**
     * 客户名称
     */
    private String name;
    /**
     * 拼音信息
     */
    private String py;
    /**
     * 客户编号
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
     * 客户类别
     */
    private String category;
    /**
     * 客户等级
     */
    private String grade;
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
     * 应收款余额
     */
    private Double balance;
    /**
     * 拓展信息
     */
    private String more;
}
