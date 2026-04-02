package com.zeroone.star.purchase.DO;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author 景旭
 * @Date 2025/10/24 11:19
 * @PackageName:com.zeroone.star.purchase.DO
 * @ClassName: imyDO
 * @Description: 收款单的 do
 * @Version 1.0.0
 */
@TableName("imy")
@Data
public class ImyDO {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "所属组织")
    @TableField("frame")
    private String frame;

    @ApiModelProperty(value = "客户")
    @TableField("customer")
    private String customer;

    @ApiModelProperty(value = "单据日期")
    @TableField("time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号")
    @TableField("number")
    private String number;

    @ApiModelProperty(value = "单据金额")
    @TableField("total")
    private BigDecimal total;

    @ApiModelProperty(value = "关联人员")
    @TableField("people")
    private String people;

    @ApiModelProperty(value = "单据附件")
    @TableField("file")
    private String file;

    @ApiModelProperty(value = "备注信息")
    @TableField("data")
    private String data;

    @ApiModelProperty(value = "扩展信息")
    @TableField("more")
    private String more;

    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]")
    @TableField("examine")
    private Integer examine;

    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]")
    @TableField("nucleus")
    private Integer nucleus;

    @ApiModelProperty(value = "制单人")
    @TableField("user")
    private String user;
}
