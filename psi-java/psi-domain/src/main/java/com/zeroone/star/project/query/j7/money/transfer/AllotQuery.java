package com.zeroone.star.project.query.j7.money.transfer;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 类名：AllotQuery
 * 包名：com.zeroone.star.project.query.j7.money.transfer
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */

@Data
@ApiModel("转账单查询列表参数")
public class AllotQuery extends PageQuery {
    @ApiModelProperty(value = "单据编号",example = "13552000")
    private String number;
    @ApiModelProperty(value = "资金账户",example = "1")
    private Integer account;
    @ApiModelProperty(value = "开始日期",example = "2025-10-26")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate time;
    @ApiModelProperty(value = "结束日期",example = "2025-10-26")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private  LocalDate leaveTime;
    @ApiModelProperty(value = "制单人",example ="1")
    private  String user;
    @ApiModelProperty(value = "审核状态",example = "1")
    private Integer examine;
    @ApiModelProperty(value = "关联人员",example ="1")
    private String people;
    @ApiModelProperty(value = "备注信息",example = "备注")
    private String data;


}
