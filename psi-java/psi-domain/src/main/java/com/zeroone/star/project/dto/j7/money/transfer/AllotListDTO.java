package com.zeroone.star.project.dto.j7.money.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 类名：AllotListDTO
 * 包名：com.zeroone.star.project.dto.j7.money.transfer
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@Data
@ApiModel("转账单列表数据对象")
@NoArgsConstructor
@AllArgsConstructor
public class AllotListDTO {
    @ApiModelProperty(value = "转账单id",example = "1")
    private String id;


    @ApiModelProperty(value = "所属组织",example = "默认组织")
    private String frame;


    @ApiModelProperty(value = "单据编号",example = "0001")
    private String number;

    @ApiModelProperty(value = "单据金额",example = "1600.00")
    private BigDecimal total;

    @ApiModelProperty(value = "单据日期",example = "2025-10-14")
    private LocalDate time;


    @ApiModelProperty(value = "审核状态",example = "1")
    private Integer examine;

    @ApiModelProperty(value = "关联人员",example ="张三")
    private String people;

    @ApiModelProperty(value = "制单人",example = "管理员")
    private String User;
}
