package com.zeroone.star.project.dto.j3.purchase;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author 加减法
 * @date 2025/10/20 20:10
 * @description 采购单列表数据对象
 * Version: 1.0
 */
@Data
@ApiModel("采购单列表数据对象")
public class PurchaseNoteListDTO {
    @ApiModelProperty(value = "主键ID", required = true, example = "BUY_ID_123456", position = 1)
    private String id;

    @ApiModelProperty(value = "所属组织（默认值：0）", required = true, example = "0", position = 3)
    private String frame;

    @ApiModelProperty(value = "供应商", required = true, example = "SUPPLIER_001", position = 4)
    private String supplier;

    @ApiModelProperty(value = "单据时间", required = true, example = "2025-10-19 09:30:00", position = 5)
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号", required = true, example = "BUY_20251019001", position = 6)
    private String number;

    @ApiModelProperty(value = "单据金额", required = true, example = "10000.0000", position = 7)
    private BigDecimal total;

    @ApiModelProperty(value = "实际金额", required = true, example = "9800.0000", position = 8)
    private BigDecimal actual;

    @ApiModelProperty(value = "单据付款", required = true, example = "9800.0000", position = 9)
    private BigDecimal buyMoney;

    @ApiModelProperty(value = "核销金额（默认值：0.0000）", required = true, example = "9800.0000", position = 9)
    private BigDecimal billMoney;

    @ApiModelProperty(value = "单据费用（默认值：0.0000）", required = true, example = "200.0000", position = 10)
    private BigDecimal cost;

    @ApiModelProperty(value = "关联人员", required = false, example = "USER_001", position = 12)
    private String people;

    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", required = true, example = "1", position = 17)
    private Integer examine;

    @ApiModelProperty(value = "核销状态[0:未核销|1:部分核销|2:已核销]", required = true, example = "2", position = 18)
    private Integer nucleus;

    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]", required = true, example = "2", position = 19)
    private Integer cse;

    @ApiModelProperty(value = "发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]", required = true, example = "2", position = 20)
    private Integer invoice;

    @ApiModelProperty(value = "核对状态[0:未核对|1:已核对]（对应数据库字段`check`）", required = true, example = "1", position = 21)
    private Integer checkStatus;

    @ApiModelProperty(value = "制单人(对应数据库buy-user)", required = true, example = "ADMIN", position = 22)
    private String noteUser;
    @ApiModelProperty(value = "备注信息", required = false, example = "季度常规补货", position = 23)
    private String data;
}
