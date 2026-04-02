package com.zeroone.star.purchase.DO;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 其它收入单核销详情
 * </p>
 *
 * @author 景旭
 * @since 2025-10-25
 */
@Data
@TableName("ice_bill")
@ApiModel(value="IceBill对象", description="其它收入单核销详情")
public class IceBillDO {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "所属单据")
    @TableField("pid")
    private String pid;

    @ApiModelProperty(value = "核销类型")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "关联单据")
    @TableField("source")
    private String source;

    @ApiModelProperty(value = "单据时间")
    @TableField("time")
    private LocalDateTime time;

    @ApiModelProperty(value = "核销金额")
    @TableField("money")
    private BigDecimal money;
}
