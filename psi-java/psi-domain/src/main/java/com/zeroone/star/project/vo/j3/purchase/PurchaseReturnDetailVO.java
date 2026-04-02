package com.zeroone.star.project.vo.j3.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnGoodsDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author xiaoliu
 * @Date 2025/10/30 16:48
 * @PackageName： com.zeroone.star.project.vo.j3.purchase
 * @ClassName: PurchaseReturnDetailVO
 * @Description: 采购退货单详情VO
 * @Version 1.0
 */
@ApiModel("PurchaseReturnDetailVO《采购退货单详情VO》")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReturnDetailVO {

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商",example = "小米")
    private String supplier;

     /**
     * 单据日期
     */
    @ApiModelProperty(value = "单据时间",example = "2025-10-30 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号",example = "CGTHD2510191431538")
    private String number;

    /**
     * 单据金额
     */
    @ApiModelProperty(value = "单据金额",example = "100")
    private BigDecimal total;

    /**
     * 实际金额
     */
    @ApiModelProperty(value = "实际金额",example = "100")
    private BigDecimal actual;

    /**
     * 实收金额
     */
    @ApiModelProperty(value = "实收金额",example = "100")
    private BigDecimal money;

    /**
     * 单据费用
     */
    @ApiModelProperty(value = "单据费用",example = "100")
    private BigDecimal cost;

    /**
     * 结算账户
     */
    @ApiModelProperty(value = "结算账户",example = "四川账户")
    private String account;

    /**
     * 关联人员
     */
    @ApiModelProperty(value = "关联人员",example = "张三")
    private String people;

    /**
     * 物流信息
     */
    @ApiModelProperty(value = "物流信息",example = "123123123123")
    private String logistics;

    /**
     * 单据附件
     */
    @ApiModelProperty(value = "单据附件",example = "http://localhost:8080/file.word")
    private String file;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息",example = "已处理")
    private String data;

    /**
     * 采购退货详细列表：PurchaseReturnDetailDTO
     */
    @ApiModelProperty(
            value = "采购退货商品详情列表",
            example = "[{\n" +
                    "  \"id\": \"212122\",\n" +
                    "  \"pid\": \"212122\",\n" +
                    "  \"goods\": \"A4纸\",\n" +
                    "  \"number\": \"P001\",\n" +
                    "  \"spec\": \"80g\",\n" +
                    "  \"attr\": \"白色\",\n" +
                    "  \"unit\": \"包\",\n" +
                    "  \"warehouse\": \"一仓\",\n" +
                    "  \"price\": 10.0,\n" +
                    "  \"nums\": 2.0,\n" +
                    "  \"total\": 20.0,\n" +
                    "  \"tax\": 0.0,\n" +
                    "  \"tat\": 0.0,\n" +
                    "  \"tpt\": 20.0,\n" +
                    "  \"data\": \"退货测试备注\"\n" +
                    "}]",
            required = true
    )
    private List<PurchaseReturnGoodsDetailDTO> details;

}
