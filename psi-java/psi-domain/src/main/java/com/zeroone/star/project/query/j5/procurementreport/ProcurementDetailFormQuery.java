package com.zeroone.star.project.query.j5.procurementreport;
import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 采购明细表查询对象
 * </p>
 *
 * @author chuming_7
 * @since 2025-10-21
 */
@ApiModel("采购明细表查询对象")
@Data
public class ProcurementDetailFormQuery extends PageQuery {
    @ApiModelProperty(value = "单据类型",example = "0",allowableValues = "0,1",
            notes = "采购单：0,退货单：1")
    private Integer type;
    @ApiModelProperty(value = "供应商",example = "张三")
    private String supplier;
    @ApiModelProperty(value = "开始日期",example = "2025-10-20")
    private String startTime;
    @ApiModelProperty(value = "结束日期",example = "2025-10-21")
    private String endTime;
    @ApiModelProperty(value = "单据编号",example = "XSD2510191703580")
    private String number;
    @ApiModelProperty(value = "仓库",example = "一号仓")
    private String warehouse;
    @ApiModelProperty(value = "备注信息",example = "这是一个备注信息!")
    private String data;
    @ApiModelProperty(value = "商品名称",example = "柴油发电机组")
    private String name;
}
