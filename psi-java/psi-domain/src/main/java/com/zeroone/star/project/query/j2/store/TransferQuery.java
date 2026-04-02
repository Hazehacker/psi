package com.zeroone.star.project.query.j2.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.query.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 18:10
 * @Description: 调拨单查询参数对象
 * @Version: 1.0
 */
@Data
@ApiModel("TransferQuery")
/*除标注出来的字段外，其他字段都能在swap表里找到*/
public class TransferQuery extends PageQuery {
    @ApiModelProperty(value = "商品名称(goods表里的字段)", example = "刀具")
    private String name;
    @ApiModelProperty(value = "单据编号",example = "0003")
    private String number;
    /*这两个字段是调拨单的时间范围，用于查询在这个时间范围内的调拨单,匹配单据时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 处理 JSON 格式请求
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 处理表单格式请求（若有）
    @ApiModelProperty(value = "起始日期", example = "2025-10-18 00:00:00")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 处理 JSON 格式请求
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 处理表单格式请求（若有）
    @ApiModelProperty(value = "截止日期", example = "2025-10-18 23:59:59")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "关联人员", example = "张三")
    private String people;
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "0")
    private Integer examine;
    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]", example = "3")
    private Integer cse;
    @ApiModelProperty(value = "制单人", example = "管理员")
    private String user;
    @ApiModelProperty(value = "备注信息", example = "测试备注信息")
    private String data;
}
