package com.zeroone.star.reportmanagement.entity.salesreport;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


/**
 * 销售退货单核销详情表
 * @author taohu5564
 * @date 2025/10/22
 */
@Getter
@Setter
@TableName("sre_bill")
public class SreBillDO {
    /*
     * 唯一id
     * */
    @TableId(value = "id")
    private Integer id;

    /*
     * 所属单据
     * */
    private Integer pId;

    /*
     * 核销类型
     * */
    private String type;

    /*
     * 关联单据
     * */
    private Integer source;

    /*
     * 单据时间
     **/
    private Integer time;

    /*
     * 核销金额
     * */
    private BigDecimal money;
}
