package com.zeroone.star.project.dto.j2.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 18:17
 * @Description: 调拨单详情数据对象
 * @Version: 1.0
 */
@Data
@ApiModel("TransferDetailDTO")
public class TransferDetailDTO {
    @ApiModelProperty(value = "单据明细", required = true)
    private ClassInfo classInfo;

    @ApiModelProperty(value = "单据费用", required = true)
    private List<costInfo> cost;

    @ApiModelProperty(value = "商品明细", required = true)
    private Info info;

    @Data
    @ApiModel("分类信息")
    public static class ClassInfo {
        @JsonFormat(pattern = "yyyy-M-d")
        @ApiModelProperty(value = "单据日期", example = "2025-10-31")
        private LocalDate time;

        @ApiModelProperty(value = "单据编号", example = "DBD2510312206576")
        private String number;

        @ApiModelProperty(value = "单据成本", example = "40.00", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
        private BigDecimal total;

        @ApiModelProperty(value = "单据费用", example = "30.00", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
        private BigDecimal cost;

        @ApiModelProperty(value = "关联人员", example = "1")
        private String people;

        @ApiModelProperty(value = "物流信息")
        private String logistics;

        @ApiModelProperty(value = "单据附件")
        private String file;

        @ApiModelProperty(value = "备注信息", example = "备注信息测试1")
        private String data;
    }

    @Data
    @ApiModel("单据费用详情")
    public static class costInfo {
        @ApiModelProperty(value = "单据费用ID", example = "39")
        private String id;

        @ApiModelProperty(value = "支出类别", example = "5")
        private String iet;

        @ApiModelProperty(value = "金额", example = "20.0000")
        private BigDecimal money;

        @ApiModelProperty(value = "备注信息", example = "单据费用测试")
        private String data;
    }

    @Data
    @ApiModel("商品信息")
    public static class Info {
        @ApiModelProperty(value = "调拨单详情ID", example = "46")
        private String id;

        @ApiModelProperty(value = "商品名称", example = "刀具", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
        private String name;

        @ApiModelProperty(value = "商品编号", example = "0003", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
        private String number;

        @ApiModelProperty(value = "规格型号", example = "DJ-0001", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
        private String spec;

        @ApiModelProperty(value = "辅助属性", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
        private String attr;

        @ApiModelProperty(value = "单位", example = "个", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
        private String unit;

        @ApiModelProperty(value = "调出仓库", example = "2")
        private String warehouse;

        @ApiModelProperty(value = "调入仓库", example = "1")
        private String storehouse;

        @ApiModelProperty(value = "批次号", example = "2")
        private String batch;

        @JsonFormat(pattern = "yyyy-M-d")
        @ApiModelProperty(value = "生产日期", example = "2025-9-23")
        private LocalDate mfd;

        @ApiModelProperty(value = "成本", example = "2")
        private BigDecimal price;

        @ApiModelProperty(value = "数量", example = "20")
        private BigDecimal nums;

        @ApiModelProperty(value = "总成本", example = "40", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
        private BigDecimal total;

        @ApiModelProperty(value = "备注信息", example = "备注信息测试2")
        private String data;
    }
}
