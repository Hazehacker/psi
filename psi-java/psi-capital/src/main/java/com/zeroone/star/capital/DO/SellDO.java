package com.zeroone.star.capital.DO;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 销售单
 * </p>
 *
 * @author 简单点
 * @since 2025-10-25
 */
@TableName("sell")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellDO {
    /**
     * 销售单id
     */
    @TableId(value="id",type= IdType.AUTO)
    @NotNull(message = "销售单id不能为空")
    private String id;

    /**
     * 关联单据
     */
    private String source;

    /**
     * 所属组织
     */
    @NotNull(message="所属组织不能为空")
    private String frame;

    /**
     * 客户
     */
    @NotNull(message="客户不能为空")
    private String customer;

    /**
     * 单据时间
     */
    @NotNull(message="单据时间为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime Time;

    /**
     * 单据编号
     */
    @NotNull(message="单据编号不能为空")
    private String number;

    /**
     * 单据金额
     */
    @NotNull(message="单据金额不能为空")
    private BigDecimal total;

    /**
     * 实际金额
     */
    @NotNull(message="实际金额不能为空")
    private BigDecimal actual;

    /**
     * 实收金额
     */
    @NotNull(message="实收不能为空")
    private BigDecimal money;

    /**
     * 单据费用
     */
    @NotNull(message="单据费用不能为空")
    private BigDecimal cost;

    /**
     * 结算账户
     */
    private String account;

    /**
     * 关联人员
     */
    private String people;

    /**
     * 物流信息
     */
    private String logistics;

    /**
     * 单据附件
     */
    private String file;

    /**
     * 备注信息
     */
    private String data;

    /**
     * 扩展信息
     */
    private String more;

    /**
     * 审核状态
     */
    @NotNull(message="审核状态不能为空")
    private Integer examine;

    /**
     * 核销状态
     */
    @NotNull(message="核销状态不能为空")
    private Integer nucleus;

    /**
     * 费用状态
     */
    @NotNull(message="费用状态不能为空")
    private Integer cse;

    /**
     * 发票状态
     */
    @NotNull(message="发票状态不能为空")
    private Integer invoice;

    /**
     * 核对状态
     */
    @NotNull(message="核对状态不能为空")
    @TableField("`check`")
    private Integer check;

    /**
     * 制单人
     */
    @NotNull(message="制单人不能为空")
    @TableField("`user`")
    private String user;
}
