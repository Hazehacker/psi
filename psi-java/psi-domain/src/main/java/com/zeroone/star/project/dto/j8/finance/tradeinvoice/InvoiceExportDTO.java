package com.zeroone.star.project.dto.j8.finance.tradeinvoice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购销发票详情DTO
 * <p>
 * 涉及数据库表：<b>invoice,buy,supplier</b>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Api("购销发票的详情对象")
public class InvoiceExportDTO {

    @ApiModelProperty(value = "单据ID", example = "233")
    private String id;

    @ApiModelProperty(value = "单据类型", example = "buy")
    private String type;

    @ApiModelProperty(value = "所属单据", example = "127")
    private String clazz;

    @ApiModelProperty(value = "开票时间", example = "2025-08-07")
    private LocalDateTime time;

    @ApiModelProperty(value = "发票号码", example = "11")
    private String number;

    @ApiModelProperty(value = "发票抬头", example = "ABC001")
    private String title;

    @ApiModelProperty(value = "开票金额", example = "123.45")
    private BigDecimal money;

    @ApiModelProperty(value = "发票附件", example = "/upload/xxx.txt")
    private String file;

    @ApiModelProperty(value = "备注信息", example = "备注test")
    private String data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Api("购销发票详情的补充信息")
    public static class InfoDTO {
        /**
         * 说明：
         * <ul>
         *   <li>使用 clazz 作为关联单据ID，到 buy 表中查询 frame、number、total、supplier</li>
         *   <li>填充 frame、number、total 字段;用 supplier 去 supplier 表中查询供应商名称填充 name</li>
         * </ul>
         */
        @ApiModelProperty(value = "所属组织", example = "组织test")
        private String frame;

        @ApiModelProperty(value = "number", example = "CCB2508071041385")
        private String number;

        @ApiModelProperty(value = "money", example = "123.45")
        private BigDecimal total;

        @ApiModelProperty(value = "往来单位/供应商名称", example = "小王")
        private String name;
    }
}
