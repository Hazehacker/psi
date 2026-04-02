package com.zeroone.star.purchase.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author 斗气化码
 * @Data 2025/10/30
 * @Description: 系统配置
 */
@Getter
@Setter
@TableName(value = "sys")
public class SysDO implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 配置内容
     */
    private String info;

    /**
     * 备注信息
     */
    private String data;
}
