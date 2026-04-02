package com.zeroone.star.project.query.j5.fundreport;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

/**
 * 单位收款查询类
 * @author 言语
 * @date 2025/10/20
 */
@Data
@ApiModel("单位收款表数据对象")
public class UnitReceiptQuery extends PageQuery {

    @ApiModelProperty(value = "单位名称")
    private String name;
    @ApiModelProperty(value = "单位编号",example = "1000001")
    private String number;
    @ApiModelProperty(value = "单位类型",example = "供应商")
    private String type;
    @ApiModelProperty(value = "备注信息" , example = "暂无")
    private String data;

}
