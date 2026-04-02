package com.zeroone.star.project.dto.j2.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel("OtherInEasyExportExcelDTO")
public class OtherInEasyExportExcelDTO {

    @ApiModelProperty(value = "所属组织", example = "默认组织")
    private String frameCv;

    @ApiModelProperty(value = "供应商", example = "郑州东方公司")
    private String supplierCv;

    @ApiModelProperty(value = "单据类型", example = "其他入库表")
    private String typeCv;

    @ApiModelProperty(value = "单据时间", example = "2025-10-16")
    private String timeCv;

    @ApiModelProperty(value = "单据编号", example = "QTRKD2510161018149")
    private String number;

    @ApiModelProperty(value = "单据成本", example = "140")
    private BigDecimal total;

    @ApiModelProperty(value = "单据费用", example = "0")
    private BigDecimal cost;

    @ApiModelProperty(value = "关联人员", example = "jack")
    private String peopleCv;

    @ApiModelProperty(value = "审核状态", example = "已审核")
    private String examineCv;

    @ApiModelProperty(value = "费用状态", example = "无需结算")
    private String cseCv;

    @ApiModelProperty(value = "核对状态", example = "已核对")
    private String checkCv;

    @ApiModelProperty(value = "制单人", example = "管理员")
    private String userCv;

    @ApiModelProperty(value = "备注信息", example = "实验1")
    private String data;
}