package com.zeroone.star.sale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "销售退货单主表实体")
@TableName("sre")
public class IsSre {

    @ApiModelProperty(value = "主键ID", example = "550e8400e29b41d4a716446655440000")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "关联销售单ID（来源单据，对应is_sell表）", example = "5")
    private String source;

    @ApiModelProperty(value = "所属组织ID（关联is_frame表）", example = "0")
    private String frame;

    @ApiModelProperty(value = "客户ID（关联is_customer表）", example = "3")
    private String customer;

    @ApiModelProperty(value = "单据时间", example = "2023-10-25 14:30:00")
    private LocalDateTime time;

    @ApiModelProperty(value = "单据编号", example = "SRE20231025001")
    private String number;

    @ApiModelProperty(value = "单据金额", example = "1500.00")
    private BigDecimal total;

    @ApiModelProperty(value = "实际金额（扣除折扣等后）", example = "1450.00")
    private BigDecimal actual;

    @ApiModelProperty(value = "实付金额（退货时的退款金额）", example = "1450.00")
    private BigDecimal money;

    @ApiModelProperty(value = "单据费用（如运费等）", example = "50.00")
    private BigDecimal cost;

    @ApiModelProperty(value = "结算账户ID（关联is_account表）", example = "2")
    private String account;

    @ApiModelProperty(value = "关联人员ID（关联is_people表）", example = "1")
    private String people;

    @ApiModelProperty(value = "备注信息", example = "客户因规格不符退货")
    private String data;

    @ApiModelProperty(value = "扩展信息（JSON格式）", example = "{}")
    private String more;

    @ApiModelProperty(value = "审核状态：0-未审核，1-已审核", example = "1")
    private Integer examine;

    @ApiModelProperty(value = "核销状态：0-未核销，1-部分核销，2-已核销", example = "2")
    private Integer nucleus;

    @ApiModelProperty(value = "费用状态：0-未结算，1-部分结算，2-已结算，3-无需结算", example = "2")
    private Integer cse;

    @ApiModelProperty(value = "发票状态：0-未开票，1-部分开票，2-已开票，3-无需开具", example = "3")
    private Integer invoice;

    @TableField(value = "`check`")
    @ApiModelProperty(value = "核对状态：0-未核对，1-已核对", example = "1")
    private Integer check;

    @ApiModelProperty(value = "制单人ID（关联is_user表）", example = "1")
    private String user;

    @TableField(exist = false)
    @ApiModelProperty(value = "关联的退货单详情列表（一对多）")
    private List<IsSreInfo> sreInfoList;

    @ApiModelProperty(value = "物流信息",example = "顺丰快递 SF123456789")
    private String logistics;  // 新增：对应表中的logistics字段

    @ApiModelProperty(value = "单据附件")
    private String file;       // 新增：对应表中的file字段

}
