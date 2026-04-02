package com.zeroone.star.project.dto.j3.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 采购单主表数据传输对象
 * @author: TWTW
 * @date: 2025/10/24
 */
@Data
@ApiModel(description = "采购单主表数据传输对象（对应is_buy表）")
public class PurchaseNoteBuyDTO {

    @ApiModelProperty(value = "供应商ID", required = true, example = "1")
    @NotNull(message = "供应商ID不能为空")
    @Min(value = 1, message = "供应商ID必须大于0（需存在有效的供应商）") // 供应商必须存在，ID至少为1
    private String supplier;

    @ApiModelProperty(value = "单据时间（Time）", required = true, example = "2025-10-30")
    @NotNull(message = "单据时间戳不能为空")
    private String time;

    @ApiModelProperty(value = "单据编号", required = true, example = "CGD2510231529348")
    @NotBlank(message = "单据编号不能为空") // 编号需有实际内容，不能为null或空白
    private String number;

    @ApiModelProperty(value = "单据金额", required = true, example = "2.0000")
    @NotNull(message = "单据金额不能为空")
    @DecimalMin(value = "0", inclusive = true, message = "单据金额不能小于0") // 金额非负
    private BigDecimal total;

    @ApiModelProperty(value = "实际金额", required = true, example = "2.0000")
    @NotNull(message = "实际金额不能为空")
    @DecimalMin(value = "0", inclusive = true, message = "实际金额不能小于0")
    private BigDecimal actual;

    @ApiModelProperty(value = "实付金额", required = true, example = "2.0000")
    @NotNull(message = "实付金额不能为空")
    @DecimalMin(value = "0", inclusive = true, message = "实付金额不能小于0")
    private BigDecimal money;

    @ApiModelProperty(value = "单据费用", required = true, example = "0.0000")
    @NotNull(message = "单据费用不能为空")
    @DecimalMin(value = "0", inclusive = true, message = "单据费用不能小于0")
    private BigDecimal cost;

    @ApiModelProperty(value = "结算账户ID",required = true, example = "1")
    @Min(value = 0, message = "结算账户ID不能小于0（若填写需为有效账户）") // 可选字段，填写时需非负
    private String account;

    @ApiModelProperty(value = "关联人员ID", required = true,example = "1")
    @Min(value = 0, message = "关联人员ID不能小于0（填写需为有效人员）")
    private String people;

    @ApiModelProperty(value = "物流信息", required = false, example = "sf12345")
    // 物流信息可选，允许null或特定格式，暂不做格式校验（可根据实际JSON规则添加@Pattern）
    private String logistics;

    // 用户上传文件
    @ApiModelProperty(value = "单据附件", required = false , example = "具体的文件。上传后，后端处理为一个文件路径，以后通过路径进行获取")
    private String file;

    @ApiModelProperty(value = "备注信息", required = false ,example = "采购订单备注信息")// 备注可选，允许null或空白，无需强制校验
    private String data;

    @ApiModelProperty(value = "关联单据|BOR(用这个来确定是否为采购订单传来非零表示生成而来的)", required = false, example = "0")
    @NotNull(message = "关联单据ID不能为空")
    @Min(value = 0, message = "关联单据ID不能小于0，默认为0") // 允许0（非生成）或正数（生成而来）
    private String source;
}