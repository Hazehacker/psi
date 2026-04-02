package com.zeroone.star.capital.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 付款单数据对象
 * 用于管理企业向供应商支付款项的单据信息，包括付款金额、审核状态、核销状态等
 * 支持付款单的创建、审核、核销等业务流程
 * </p>
 *
 * @author Junjie
 * @since 2025-10-25
 */
@TableName("omy")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OmyDO {
    /**
     * 付款单唯一标识符
     * 主键，用于唯一标识一张付款单
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @NotNull(message = "付款单id不能为空")
    private String id;

    /**
     * 所属组织编号
     * 标识该付款单所属的组织机构，默认值为'0'
     */
    @NotNull(message = "所属组织不能为空")
    private String frame;

    /**
     * 供应商编号
     * 关联供应商信息，标识付款的收款方
     */
    private String supplier;

    /**
     * 单据时间
     * 记录付款单的创建或生效时间
     */
    @NotNull(message = "单据时间不能为空")
    private LocalDateTime time;

    /**
     * 单据编号
     * 付款单的业务编号，用于业务查询和追溯
     */
    @NotNull(message = "单据编号不能为空")
    private String number;

    /**
     * 单据金额
     * 本次付款的总金额，精确到小数点后4位
     */
    @NotNull(message = "单据金额不能为空")
    private BigDecimal total;

    /**
     * 关联人员编号
     * 关联的业务人员或经办人信息
     */
    private String people;

    /**
     * 单据附件
     * 存储付款单相关的附件文件路径或URL，如合同、发票等
     */
    private String file;

    /**
     * 备注信息
     * 记录付款单的补充说明或特殊备注
     */
    private String data;

    /**
     * 扩展信息
     * 以JSON或其他格式存储的扩展字段，用于业务扩展
     */
    private String more;

    /**
     * 审核状态
     * 0-未审核：付款单待审核
     * 1-已审核：付款单已通过审核
     */
    @NotNull(message = "审核状态不能为空")
    private Integer examine;

    /**
     * 核销状态
     * 0-未核销：付款单尚未进行核销
     * 1-部分核销：付款单已部分核销，还有未核销金额
     * 2-已核销：付款单已全部核销完成
     */
    @NotNull(message = "核销状态不能为空")
    private Integer nucleus;

    /**
     * 制单人
     * 创建该付款单的用户编号或用户名
     */
    @NotNull(message = "制单人不能为空")
    private String user;
}
