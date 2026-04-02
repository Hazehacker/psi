package com.zeroone.star.project.query.j3.capital;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

/**
 * @author 加减法
 * @date 2025/10/20 19:56
 * @description 获取采购单列表查询参数
 * Version: 1.0
 */
@SuppressWarnings("all")
@Data
@ApiModel("获取采购单列表查询参数")
public class PurchaseNoteQuery extends PageQuery {
    @ApiModelProperty(value = "商品名称", example = "小刀")
    @NotNull(message = "商品名称不能为空")
    private String name;

    //bre
    @ApiModelProperty(value = "单据编号", example = "PO20251019001")
    @NotNull(message = "单据编号不能为空")
    private String number;

    //bre
    @ApiModelProperty(value = "供应商名称", example = "供应商")
    @NotNull(message = "供应商名称不能为空")
    private String supplier;

    //bre
    @ApiModelProperty(value = "制单人", example = "ADMIN")
    @NotNull(message = "制单人不能为空")
    private String user;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String noteUser;
    @ApiModelProperty(value = "开始时间", example = "2025-10-01")
    private String startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间", example = "2025-10-31")
    private String endTime;

    //bre
    @ApiModelProperty(value = "关联人员", example = "USER_001")
    private String people;

    //bre
    @ApiModelProperty(value = "审核状态 [0:未审核|1:已审核]", example = "1")
    private Integer examine;

    //bre
    @ApiModelProperty(value = "核销状态 [0:未核销|1:部分核销|2:已核销]", example = "2")
    private Integer nucleus;

    //bre
    @ApiModelProperty(value = "费用状态 [0:未结算|1:部分结算|2:已结算|3:无需结算][0:未结算|1:部分结算|2:已结算|3:无需结算]", example = "2")
    private Integer cse;

    //bre
    @ApiModelProperty(value = "发票状态 [0:未开票|1:部分开票|2:已开票|3:无需开具]", example = "2")
    private Integer invoice;

    //bre
    @ApiModelProperty(value = "核对状态 [0:未核对|1:已核对]", example = "1")
    private Integer check;

    //bre
    @ApiModelProperty(value = "核销状态[0:未核销|1:已核销]（对应数据库字段`check`）", example = "1")
    private Integer checkStatus;
    @ApiModelProperty(value = "备注信息", example = "季度常规补货")
    private String data;
}
