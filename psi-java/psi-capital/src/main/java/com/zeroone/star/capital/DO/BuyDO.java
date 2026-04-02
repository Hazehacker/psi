package com.zeroone.star.capital.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 采购单 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("buy")
public class BuyDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /** 关联单据|BOR */
    private String source;

    /** 所属组织 */
    private String frame;

    /** 供应商 */
    private String supplier;

    /** 单据时间 */
    private LocalDateTime time;

    /** 单据编号 */
    private String number;

    /** 单据金额 */
    private BigDecimal total;

    /** 实际金额 */
    private BigDecimal actual;

    /** 实付金额 */
    private BigDecimal money;

    /** 单据费用 */
    private BigDecimal cost;

    /** 结算账户 */
    private String account;

    /** 关联人员 */
    private String people;

    /** 物流信息 */
    private String logistics;

    /** 单据附件 */
    private String file;

    /** 备注信息 */
    private String data;

    /** 扩展信息 */
    private String more;

    /** 审核状态[0:未审核|1:已审核] */
    private Integer examine;

    /** 核销状态[0:未核销|1:部分核销|2:已核销] */
    private Integer nucleus;

    /** 费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算] */
    private Integer cse;

    /** 发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具] */
    private Integer invoice;

    /** 核对状态[0:未核对|1:已核对] */
    @TableField("`check`")
    private Boolean check;

    /** 制单人 */
    @TableField("`user`")
    private String user;
}
