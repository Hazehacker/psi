package com.zeroone.star.project.vo.j3.purchase;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xiaoliu
 * @Data 2025/10/29
 * @Description: 采购退货单列表VO
 */
@ApiModel("PurchaseReturnListVO《采购退货单列表VO》")
@Getter
@Setter
public class PurchaseReturnListVO implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键id",example = "1")
    private String id;

    /**
     * 关联单据|BUY
     */
    @ApiModelProperty(value = "关联单据",example = "1010100101")
    private String source;

    /**
     * 所属组织
     */
    @ApiModelProperty(value = "所属组织",example = "默认组织")
    private String frame;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商",example = "郑州东方之花医药股份有限公司")
    private String supplier;

    /**
     * 单据时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "单据时间",example = "2025-10-30 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号",example = "1011010")
    private String number;

    /**
     * 单据金额
     */
    @ApiModelProperty(value = "单据金额",example = "100")
    private BigDecimal total;

    /**
     * 实际金额
     */
    @ApiModelProperty(value = "实际金额",example = "100")
    private BigDecimal actual;

    /**
     * 实收金额
     */
    @ApiModelProperty(value = "实收金额",example = "100")
    private BigDecimal money;

    /**
     * 核销金额 ：bre_bill表
     */
    @ApiModelProperty(value = "核销金额",example = "100")
    private BigDecimal writeOffAmount;

    /**
     * 单据费用
     */
    @ApiModelProperty(value = "单据费用",example = "0")
    private BigDecimal cost;

    /**
     * 关联人员
     */
    @ApiModelProperty(value = "关联人员",example = "张三")
    private String people;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    @ApiModelProperty(value = "审核状态",example = "1")
    private Integer examine;

    /**
     * 核销状态[0:未核销|1:部分核销|2:已核销]
     */
    @ApiModelProperty(value = "核销状态",example = "1")
    private Integer nucleus;

    /**
     * 费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]
     */
    @ApiModelProperty(value = "费用状态",example = "1")
    private Integer cse;

    /**
     * 发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]
     */
    @ApiModelProperty(value = "发票状态",example = "1")
    private Integer invoice;

    /**
     * 核对状态[0:未核对|1:已核对]
     */
    @ApiModelProperty(value = "核对状态",example = "1")
    private Integer check;

    /**
     * 制单人
     */
    @ApiModelProperty(value = "制单人",example = "1")
    @TableField(fill = FieldFill.INSERT)
    private String user;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息",example = "小米供应")
    private String data;


}
