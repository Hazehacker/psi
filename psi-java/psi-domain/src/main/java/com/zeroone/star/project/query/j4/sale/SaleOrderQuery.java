package com.zeroone.star.project.query.j4.sale;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * <p>
 * 描述：销售订单查询参数
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("销售订单查询参数")
public class SaleOrderQuery extends PageQuery {
    @ApiModelProperty(value = "销售订单id",example = "1")
    private String id;
    @ApiModelProperty(value = "所属组织",example = "01星球")
    private String frame;
    @ApiModelProperty(value = "客户",example = "张三")
    private String customer;
    @ApiModelProperty(value = "单据时间",example = "2020-01-01")
    private LocalDateTime time;
    @ApiModelProperty(value = "单据编号",example = "XS123456789")
    private String number;
    @ApiModelProperty(value = "单据金额",example = "36.0")
    private BigDecimal total;
    @ApiModelProperty(value = "实际金额",example = "36.0")
    private BigDecimal actual;
    @ApiModelProperty(value = "关联人员",example = "李四")
    private String people;
    @ApiModelProperty(value = "到货日期",example = "2020-01-01")
    private LocalDateTime arrival;
    @ApiModelProperty(value = "物流信息",example = "XS123456789")
    private String logistics;
    @ApiModelProperty(value = "单据附件",example = "picture.png")
    private String file;
    @ApiModelProperty(value = "备注信息",example = "备注信息")
    private String data;
    @ApiModelProperty(value = "扩展信息",example = "扩展信息")
    private String more;
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]",example = "1")
    private Integer examine;
    @ApiModelProperty(value = "出库状态[0:未出库|1:部分出库|2:已出库|3:关闭]",example = "1")
    private Integer state;
    @ApiModelProperty(value = "制单人",example = "王五")
    private String user;
}
