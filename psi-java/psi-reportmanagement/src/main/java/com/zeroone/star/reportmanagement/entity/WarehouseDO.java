package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 仓库
 * @author rainsilent
 * @date 2025/10/22
 */
@Getter
@Setter
@TableName("warehouse")
public class WarehouseDO {
    /**
     * 唯一id
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 仓库名称
     */
    private String name;
    /**
     * 仓库编号
     */
    private String number;
    /**
     * 所属组织
     */
    private String frame;
    /**
     * 联系人员
     */
    private String contacts;
    /**
     * 联系电话
     */
    private String tel;
    /**
     * 仓库地址
     */
    private String add;
    /**
     * 备注信息
     */
    private String data;
}
