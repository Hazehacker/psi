package com.zeroone.star.project.query.j4.sale;


import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("销售退货单查询参数")
public class SaleReturnQuery extends PageQuery {
    @ApiModelProperty(value = "商品名称", example = "刀具")
    private String goods;
    @ApiModelProperty(value = "单据编号", example = "CGTHD2510182208099")
    private String id;
    @ApiModelProperty(value = "客户", example = "王虎")
    private String customer;
    @ApiModelProperty(value = "关联人员", example = "张三")
    private String people;
    @ApiModelProperty(value = "开始时间", example = "2025-08-22")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;
    @ApiModelProperty(value = "结束时间", example = "2025-09-21")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
    @ApiModelProperty(value = "制单人", example = "张三")
    private String user;
    @ApiModelProperty(value = "审核状态 (0: 未审核 1: 已审核)", example = "1")
    private Integer examine;
    @ApiModelProperty(value = "核销状态 (0: 未核销 1: 部分核销 2: 已核销)", example = "2")
    private Integer nucleus;
    @ApiModelProperty(value = "费用状态(0: 未结算 1: 部分结算 2: 已结算 3: 无需结算)", example = "2")
    private Integer cse;
    @ApiModelProperty(value = "发票状态 (0: 未开票 1: 部分开票 2: 已开票 3: 无需开票)", example = "2")
    private Integer invoice;
    @ApiModelProperty(value = "核对状态 (0: 未核对 1: 已核对)", example = "1")
    private Integer check;
    @ApiModelProperty(value = "备注信息", example = "xxx")
    private String data;

}
