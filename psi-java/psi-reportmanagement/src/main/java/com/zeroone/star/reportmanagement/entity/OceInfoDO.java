package com.zeroone.star.reportmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 其它支出单详情
 * @author 天天困
 * @date 2025/10/23
 */
@Getter
@Setter
@TableName("oce_info")
public class OceInfoDO {

    /**
     * 唯一id（主键）
     */
    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /**
     * 所属ID
     */
    private String pid;

    /**
     * 所属费用
     */
    private String source;

    /**
     * 收支类型
     */
    private String iet;

    /**
     * 结算金额
     */
    private BigDecimal money;

    /**
     * 备注信息
     */
    private String data;
}
