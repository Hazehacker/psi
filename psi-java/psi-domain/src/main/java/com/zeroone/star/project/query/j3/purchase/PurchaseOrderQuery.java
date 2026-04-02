package com.zeroone.star.project.query.j3.purchase;

import com.zeroone.star.project.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 唐怀瑟
 * @Data 2025/10/19 16:21
 * @PackageName: com.zeroone.star.project.query.j3.purchase
 * @CLASSNAME: PurchaseOrderQuery
 * @Description: 采购订单查询
 * @Version 1.0
 */

@Data
@ApiModel("采购订单查询参数")
public class PurchaseOrderQuery extends PageQuery {
    @ApiModelProperty(value = "商品名称", example = "小刀")
    @Size(max = 32, message = "商品名称长度不能超过32个字符")
    private String name;
    @ApiModelProperty(value = "单据编号", example = "CGDD2510191455258")
    @Size(max = 32, message = "单据编号长度不能超过32个字符")
    private String number;
    @ApiModelProperty(value = "供应商", example = "郑州东方之花医药股份有限公司")
    @Size(max = 32, message = "供应商长度不能超过32个字符")
    private String supplier;
    @ApiModelProperty(value = "关联人员", example = "jack")
    @Size(max = 32, message = "关联人员长度不能超过32个字符")
    private String people;
    @ApiModelProperty(value = "单据开始时间", example = "2025-10-18")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String documentStartTime;
    @ApiModelProperty(value = "单据结束时间", example = "2025-10-19")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String documentEndTime;
    @ApiModelProperty(value = "到货开始时间", example = "2025-10-18")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String arrivalStartTime;
    @ApiModelProperty(value = "到货结束时间", example = "2025-10-19")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String arrivalEndTime;
    @ApiModelProperty(value = "制单人", example = "管理员")
    @Size(max = 32, message = "制单人长度不能超过32个字符")
    private String user;
    @ApiModelProperty(value = "审核状态", example = "已审核")
    private String examine;
    @ApiModelProperty(value = "入库状态", example = "已入库")
    private String state;
    @ApiModelProperty(value = "备注信息", example = "备注")
    private String data;
}
