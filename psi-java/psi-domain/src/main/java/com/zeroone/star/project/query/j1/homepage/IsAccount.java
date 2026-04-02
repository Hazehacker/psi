package com.zeroone.star.project.query.j1.homepage;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资金账户
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("is_account")
@ApiModel(value="IsAccount对象", description="资金账户")
public class IsAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "账户名称")
    private String name;

    @ApiModelProperty(value = "账户编号")
    private String number;

    @ApiModelProperty(value = "所属组织")
    private Integer frame;

    @ApiModelProperty(value = "余额日期")
    private Integer time;

    @ApiModelProperty(value = "期初余额")
    private BigDecimal initial;

    @ApiModelProperty(value = "账户余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "备注信息")
    private String data;


}
