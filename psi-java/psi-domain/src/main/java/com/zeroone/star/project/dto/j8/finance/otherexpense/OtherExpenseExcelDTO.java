package com.zeroone.star.project.dto.j8.finance.otherexpense;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OtherExpenseExcelDTO {
    /**
     * 主表信息
     */
    private ClassInfo classInfo;
    /**
     * 明细列表
     */
    private List<InfoItem> info;

    @Data
    public static class ClassInfo {
        @ApiModelProperty(value = "供应商")
        private String supplier;
        @ApiModelProperty(value = "单据日期")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private String time;
        @ApiModelProperty(value = "单据编号")
        private String pid;
        @ApiModelProperty(value = "总条数")
        private String number;
        @ApiModelProperty(value = "总金额(单据金额)")
        private String total;
        @ApiModelProperty(value = "实际金额")
        private String actual;
        @ApiModelProperty(value = "实付金额")
        private String money;
        @ApiModelProperty(value = "结算账户")
        private String account;
        @ApiModelProperty(value = "关联人员")
        private String people;
        @ApiModelProperty(value = "备注信息")
        private String data;
        @ApiModelProperty(value = "盖章url")
        private String nucleusUrl;
    }

    @Data
    public static class InfoItem {
        @ApiModelProperty(value = "序号")
        private String id;
        @ApiModelProperty(value = "支出类别")
        private String type;
        @ApiModelProperty(value = "结算金额")
        private String money;
        @ApiModelProperty(value = "备注")
        private String remark;
    }
}
