package com.zeroone.star.purchase.DO;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 源单
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-25
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sor")
public class SorDO {

    /**
     * 销售单id
     */
    @TableId(value="id",type= IdType.AUTO)
    @NotNull(message = "销售订单id不能为空")
    private String id;


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
    private DateTime dateTime;

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
     * 关联人员
     */
    private String people;

    /**
     * 到货日期
     */
    @NotNull(message="到货日期不能为空")
    private Date arrival;

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
     * 出库状态
     */
    @NotNull(message="出库状态不能为空")
    private Integer state;

    /**
     * 制单人
     */
    @NotNull(message="制单人不能为空")
    private String user;
}
