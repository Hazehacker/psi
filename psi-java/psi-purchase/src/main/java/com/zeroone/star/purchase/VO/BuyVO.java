package com.zeroone.star.purchase.VO;

import com.zeroone.star.purchase.DO.BuyDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
//callSuper = true确保在equals/hashCode时也比较父类(BuyDO)的字段
@EqualsAndHashCode(callSuper = true)
@ApiModel("采购单VO")
public class BuyVO extends BuyDO {
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
}
