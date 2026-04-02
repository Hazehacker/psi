package com.zeroone.star.project.dto.j8.finance.otherexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 描述：其它支出单数据对象
 *
 * 涉及数据库表：oce
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("其他支出单数据对象")
public class OtherExpenseViewDTO {
    @ApiModelProperty(value = "支出单id",example = "test1")
    private String id;
    @ApiModelProperty(value = "所属组织",example = "默认组织")
    private String frame;
    @ApiModelProperty(value = "供应商",example = "塑料供应商")
    private String supplier;
    @ApiModelProperty(value = "单据时间",example = "2023-05-19 00:00:00")
    private LocalDateTime time;
    @ApiModelProperty(value = "单据编号",example = "QTZCD2510211359551")
    private String number;
    @ApiModelProperty(value = "单据金额",example = "100")
    private BigDecimal total;
    @ApiModelProperty(value = "实际金额",example = "100")
    private BigDecimal actual;
    @ApiModelProperty(value = "实付金额",example = "100")
    private BigDecimal money;
    @ApiModelProperty(value = "结算账户",example = "四川账户")
    private String account;
    @ApiModelProperty(value = "关联人员",example = "张三")
    private String people;
    @ApiModelProperty(value = "单据附件",example = "test1")
    private String file;
    @ApiModelProperty(value = "备注信息",example = "无")
    private String data;
    @ApiModelProperty(value = "扩展信息",example = "test1")
    private String more;
    @ApiModelProperty(value = "审核状态", example = "1")
    private int examine;
    @ApiModelProperty(value = "核销状态", example = "1")
    private int nucleus;
    @ApiModelProperty(value = "制单人",example = "管理员")
    private String user;
}
