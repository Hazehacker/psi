package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: 斗气化码
 * @CreateTime: 2025-10-20
 * @Description: 采购退货单参数
 * @Version: 1.0
 */
@Data
@ApiModel("采购退货单参数")
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReturnBreDTO {
    @ApiModelProperty(value = "主键", example = "A000001")
    private String id;
    @ApiModelProperty(value = "关联单据", example = "A000002")
    private String source;
    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String frame;
    @ApiModelProperty(value = "供应商", example = "小米")
    private String supplier;
    @ApiModelProperty(value = "单据时间", example = "2025-01-01")
    private LocalDateTime time;
    @ApiModelProperty(value = "单据编号", example = "XM001")
    private String number;
    @ApiModelProperty(value = "单据金额", example = "3999.00")
    private double total;
    @ApiModelProperty(value = "实际金额", example = "3599.00")
    private double actual;
    @ApiModelProperty(value = "实收金额", example = "3299.00")
    private double money;
    @ApiModelProperty(value = "单据费用", example = "3999.00")
    private double cost;
    @ApiModelProperty(value = "结算账户", example = "支付宝")
    private String account;
    @ApiModelProperty(value = "关联人员", example = "张三")
    private String people;
    @ApiModelProperty(value = "物流信息", example = "1234567890")
    private String logistics;
    @ApiModelProperty(value = "单据附件")
    private String file;
    @ApiModelProperty(value = "备注信息", example = "批量导入")
    private String data;
    @ApiModelProperty(value = "扩展信息")
    private String more;
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "1")
    private int examine;
    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]", example = "2")
    private int nucleus;
    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]", example = "2")
    private int cse;
    @ApiModelProperty(value = "发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]", example = "2")
    private int invoice;
    @ApiModelProperty(value = "核对状态[0:未核对|1:已核对]", example = "1")
    private int check;
    @ApiModelProperty(value = "制单人", example = "张三")
    private double user;
}
