package com.zeroone.star.project.dto.j2.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@ApiModel("TransferDetailListDTO")
/*除额外标注，其余字段都能在swap_info表里找到*/
public class TransferDetailListDTO {
    @ApiModelProperty(value = "隐藏属性唯一id",required = true , example = "1")
    private String id;
    @ApiModelProperty(value = "所属id，应该跟swap里的id对应",required = true, example = "1")
    private String pid;
    @ApiModelProperty(value = "所属商品，应该跟goods里的id对应",required = true, example = "1")
    private String goods;
    @ApiModelProperty(value = "商品名称，goods表里",required = true, example = "刀具")
    private String name;
    @ApiModelProperty(value = "商品编号，goods表里",required = true, example = "0003")
    private String number;
    @ApiModelProperty(value = "规格型号，goods表里", example = "DJ-0001")
    private String spec;
    @ApiModelProperty(value = "辅助属性", example = "测试")
    private String attr;
    @ApiModelProperty(value = "单位", required = true , example = "个")
    private String unit;
    @ApiModelProperty(value = "调出仓库",required = true, example = "测试仓库1")
    private String warehouse;
    @ApiModelProperty(value = "调入仓库",required = true, example = "测试仓库2")
    private String storehouse;
    @ApiModelProperty(value = "批次", example = "1")
    private String batch;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // Spring 框架解析用（如接收请求参数）
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") // Jackson 序列化/反序列化用
    @ApiModelProperty(value = "生产日期", example = "2025-10-18")
    private LocalDate mfd;
    @ApiModelProperty(value = "成本",required = true,example = "2")
    private BigDecimal price;
    @ApiModelProperty(value = "数量",required = true, example = "1")
    private BigDecimal nums;
    @ApiModelProperty(value = "总成本",required = true, example = "2")
    private BigDecimal total;
    @ApiModelProperty(value = "备注信息", example = "测试备注信息")
    private String data;

    //扩展,以下数据来自swap表
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 处理 JSON 格式请求
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 处理表单格式请求（若有）
    @ApiModelProperty(value = "单据日期（时间）",required = true , example = "2025-10-18 00:00:00")
    private LocalDateTime time;
    @ApiModelProperty(value = "单据编号，需要映射",required = true , example = "DJ20251018001")
    private String swapNumber;
    @ApiModelProperty(value = "单据成本,需要映射",required = true , example = "12")
    private BigDecimal swapTotal;
    @ApiModelProperty(value = "单据费用",required = true , example = "0")
    private BigDecimal cost;
    @ApiModelProperty(value = "关联人员", example = "张三")
    private String people;
    @ApiModelProperty(value = "物流信息", example = "")
    private LogisticsDTO logistics;
    @ApiModelProperty(value = "单据附件", example = "测试数据3")
    private String file;
    @ApiModelProperty(value = "备注信息,需要映射", example = "测试数据2")
    private String swapData;

    @Data
    @ApiModel("物流信息实体")
    public static class LogisticsDTO {
        @ApiModelProperty(value = "物流标识", example = "auto")
        private String key;
        @ApiModelProperty(value = "物流名称", example = "自动识别")
        private String name;
        @ApiModelProperty(value = "物流编号", example = "123456")
        private String number;
    }
}

