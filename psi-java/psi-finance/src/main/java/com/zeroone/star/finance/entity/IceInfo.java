package com.zeroone.star.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 其它收入单详情
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Getter
@Setter
@TableName("ice_info")
public class IceInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 所属ID
     */
    private String pid;

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


    @TableField(exist = false)
    private Iet ietData;


}
