package com.zeroone.star.capital.DO;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: 小王
 * @description: 核销单详情实体
 * @create: 2025-10-24
 * @Version: 1.0
 **/
@Data
@ApiModel("核销单详情实体")
@TableName("bill_info")
@AllArgsConstructor
@NoArgsConstructor
public class BillInfoDO {

    @ApiModelProperty(value = "核销类型", example = "预收冲应收")
    private String bill;

    @ApiModelProperty(value = "id", example = "20001")
    private String id;

    @ApiModelProperty(value = "单据类型", example = "销售单")
    private String mold;

    @ApiModelProperty(value = "核销金额", example = "500.0000")
    private BigDecimal money;

    @ApiModelProperty(value = "所属ID", example = "10001")
    private String pid;

    @ApiModelProperty(value = "关联单据", example = "HXD20231201001")
    private String source;
}
