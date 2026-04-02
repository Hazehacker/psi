package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author xiaoliu
 * @Date 2025/10/19 13:47
 * @PackageName:com.zeroone.star.project.dto.j3.purchase
 * @ClassName: PurchaseReturnDTO
 * @Description: 采购退货单查询数据对象
 * @Version 1.0
 */
@ApiModel("采购退货单DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class PurchaseReturnListDTO {

    /**
     * 所属组织
     */
    @ApiModelProperty(value = "所属组织",example = "默认组织")
    @Size(max = 50, message = "所属组织名称长度不能超过50字符")
    private String frame;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商",example = "小米")
    @Size(max = 100, message = "供应商名称长度不能超过100字符")
    private String supplier;

    /**
     * 单据时间：转为时间戳存储到数据库
     */
    @ApiModelProperty(value = "单据时间",example = "2025-10-19")
    @PastOrPresent(message = "单据时间不能是未来时间")
    private LocalDateTime time;

    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号",example = "CGTHD2510191431538")
    @Size(max = 50, message = "单据编号长度不能超过50字符")
    @Pattern(regexp = "^[A-Z0-9-]*$", message = "单据编号只能包含大写字母、数字和横线")
    private String number;

    /**
     * 单据金额
     */
    @ApiModelProperty(value = "单据金额",example = "100")
    @DecimalMin(value = "0.00", message = "单据金额不能为负数")
    private BigDecimal total;

    /**
     * 实际金额
     */
    @ApiModelProperty(value = "实际金额",example = "100")
    @DecimalMin(value = "0.00", message = "实际金额不能为负数")
    private BigDecimal actual;

    /**
     * 实收金额
     */
    @ApiModelProperty(value = "实收金额",example = "100")
    @DecimalMin(value = "0.00", message = "实收金额不能为负数")
    private BigDecimal money;

    /**
     * 核销金额
     */
    @ApiModelProperty(value = "核销金额",example = "小刀")
    @DecimalMin(value = "0.00", message = "核销金额不能为负数")
    private BigDecimal verificationMoney;

    /**
     * 单据费用
     */
    @ApiModelProperty(value = "单据费用",example = "100")
    @DecimalMin(value = "0.00", message = "单据费用不能为负数")
    private BigDecimal cost;

    /**
     * 结算账户
     */
    @ApiModelProperty(value = "结算账户",example = "四川账户")
    @Size(max = 50, message = "结算账户名称长度不能超过50字符")
    private String account;

    /**
     * 关联人员
     */
    @ApiModelProperty(value = "关联人员",example = "张三")
    @Size(max = 20, message = "关联人员姓名长度不能超过20字符")
    private String people;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    @ApiModelProperty(value = "审核状态",example = "1")
    @Min(value = 0, message = "审核状态值无效")
    @Max(value = 1, message = "审核状态值无效")
    private Integer examine;

    /**
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     */
    @ApiModelProperty(value = "核销状态",example = "1")
    @Min(value = 0, message = "核销状态值无效")
    @Max(value = 2, message = "核销状态值无效")
    private Integer nucleus;

    /**
     * 费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]
     */
    @ApiModelProperty(value = "费用状态",example = "1")
    @Min(value = 0, message = "费用状态值无效")
    @Max(value = 3, message = "费用状态值无效")
    private Integer cse;

    /**
     * 发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]
     */
    @ApiModelProperty(value = "发票状态",example = "1")
    @Min(value = 0, message = "发票状态值无效")
    @Max(value = 3, message = "发票状态值无效")
    private Integer invoice;

    /**
     * 核对状态[0:未核对|1:已核对]
     */
    @ApiModelProperty(value = "核对状态",example = "1")
    @Min(value = 0, message = "核对状态值无效")
    @Max(value = 1, message = "核对状态值无效")
    private Integer check;

    /**
     * 制单人
     */
    @ApiModelProperty(value = "制单人",example = "123")
    @Size(max = 50, message = "制单人长度不能超过50字符")
    private String user;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息",example = "已处理")
    @Size(max = 500, message = "备注信息长度不能超过500字符")
    private String data;


}
