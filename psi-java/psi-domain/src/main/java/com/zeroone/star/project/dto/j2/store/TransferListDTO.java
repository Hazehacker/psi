package com.zeroone.star.project.dto.j2.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 18:17
 * @Description: 调拨单数据对象
 * @Version: 1.0
 */
@Data
@ApiModel("TransferListDTO")
/**除额外标注，其余字段都能在swap表里找到*/
public class TransferListDTO {
    @ApiModelProperty(value = "隐藏属性唯一id，但他应该是调拨单详细的id，" +
            "属于swap_info表，因为pid可以重复，这样会导致错误", required = true ,example = "1")
    private String id;
    @ApiModelProperty(value = "隐藏属性商品名称，查询需要,goods表",required = true , example = "默认组织")
    private String name;
    @ApiModelProperty(value = "所属组织",required = true , example = "默认组织")
    private String frame;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 处理 JSON 格式请求
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 处理表单格式请求（若有）
    @ApiModelProperty(value = "单据时间",required = true , example = "2025-10-18")
    private LocalDateTime time;
    @ApiModelProperty(value = "单据编号",required = true , example = "DJ20251018001")
    private String number;
    @ApiModelProperty(value = "单据成本",required = true , example = "12")
    private BigDecimal total;
    @ApiModelProperty(value = "单据费用",required = true , example = "0")
    private BigDecimal cost;
    @ApiModelProperty(value = "关联人员", example = "张三")
    private String people;
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "0")
    private int examine;
    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]", example = "3")
    private int cse;
    @ApiModelProperty(value = "制单人",required = true , example = "管理员")
    private String user;
    @ApiModelProperty(value = "备注信息", example = "测试备注信息")
    private String data;



}

