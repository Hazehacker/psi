package com.zeroone.star.project.query.j3.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author xiaoliu
 * @Date 2025/10/19 14:27
 * @PackageName:com.zeroone.star.project.query.j3.purchase
 * @ClassName: PurchaseReturnQuery
 * @Description: 采购退货单分页查询参数对象
 * @Version 1.0
 */
@Data
@ApiModel("PurchaseReturnQuery《采购退货单分页查询参数》")
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseReturnQuery extends PageQuery {

    @ApiModelProperty(value = "商品名称",example = "A4打印纸")
    private String name;

    @ApiModelProperty(value = "单据编号",example = "CGTHD2510191434938")
    private String number;

    @ApiModelProperty(value = "供应商", example = "小米科技")
    private String supplier;

    @ApiModelProperty(value = "制单人", example = "xiaoliu")
    private String createUser;

    @ApiModelProperty(value = "单据开始日期", example = "2024-10-19 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime timeStart;

    @ApiModelProperty(value = "单据结束日期", example = "2026-10-19 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime timeEnd;

    @ApiModelProperty(value = "关联人员", example = "张三")
    private String people;

    @ApiModelProperty(value = "审核状态",example = "0")
    private Integer examine;

    @ApiModelProperty(value = "核销状态", example = "0")
    private Integer nucleus;

    @ApiModelProperty(value = "费用状态", example = "0")
    private Integer cse;

    @ApiModelProperty(value = "发票状态", example = "0")
    private Integer invoice;

    @ApiModelProperty(value = "核对状态", example = "0")
    private Integer checkStatus;

    @ApiModelProperty(value = "备注信息",example = "采购退货：部分商品包装破损")
    private String data;

}
