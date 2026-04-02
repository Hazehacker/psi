package com.zeroone.star.project.query.j4.sale;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 销售退货单核销详情
 * </p>
 *
 * @author author
 * @since 2025-10-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sre_bill")
public class SaleSreBillQuery {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 所属单据
     */
    private String pid;

    /**
     * 核销类型
     */
    private String type;

    /**
     * 关联单据
     */
    private String source;

    /**
     * 单据时间
     */
    private LocalDateTime time;

    /**
     * 核销金额
     */
    private BigDecimal money;


}
