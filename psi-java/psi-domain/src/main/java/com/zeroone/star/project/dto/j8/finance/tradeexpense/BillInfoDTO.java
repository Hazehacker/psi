package com.zeroone.star.project.dto.j8.finance.tradeexpense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

@Data
@ApiModel("购销费用详情")
public class BillInfoDTO extends BillDTO {

    @ApiModelProperty(value = "所属ID",example = "1")
    private Integer pid;
    @ApiModelProperty(value = "关联单据",example = "1")
    private Integer source;
    @ApiModelProperty(value = "核销类型",example = "1")
    private String bill;
    @ApiModelProperty(value = "单据类型",example = "10")
    private String mold;
    @ApiModelProperty(value = "核销金额",example = "10")
    private Decimal money;
}
