package com.zeroone.star.capital.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: 小王
 * @description: 核销单实体
 * @create: 2025-10-24
 * @Version: 1.0
 **/
@Data
@ApiModel("核销单实体")
@TableName("bill")
@AllArgsConstructor
@NoArgsConstructor
public class BillDO {

    @ApiModelProperty(value = "客户", example = "张三客户")
    private String customer;

    @ApiModelProperty(value = "备注信息", example = "这是备注")
    private String data;

    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "0")
    private Integer examine;

    @ApiModelProperty(value = "单据附件", example = "附件路径xxx")
    private String file;

    @ApiModelProperty(value = "所属组织", example = "研发部")
    private String frame;

    @ApiModelProperty(value = "id", example = "10001")
    private String id;

    @ApiModelProperty(value = "扩展信息", example = "扩展内容xxx")
    private String more;

    @ApiModelProperty(value = "单据编号", example = "DOC20251024001")
    private String number;

    @ApiModelProperty(value = "关联人员", example = "李四")
    private String people;

    @ApiModelProperty(value = "总核金额", example = "1000.0000")
    private BigDecimal pmy;

    @ApiModelProperty(value = "总销金额", example = "2000.0000")
    private BigDecimal smp;

    @ApiModelProperty(value = "供应商", example = "XX供应商公司")
    private String supplier;

    @ApiModelProperty(value = "单据时间", example = "2025-10-24")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date time;

    @ApiModelProperty(value = "核销类型[0:预收冲应收|1:预付冲应付...]", example = "0")
    private Integer type;

    @ApiModelProperty(value = "制单人", example = "王五")
    private String user;
}
