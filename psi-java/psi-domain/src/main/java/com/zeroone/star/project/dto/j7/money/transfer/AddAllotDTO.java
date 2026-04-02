package com.zeroone.star.project.dto.j7.money.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 类名：AddAllotDTO
 * 包名：com.zeroone.star.project.dto.j7.money.transfer
 * 描述：新增转账单
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@Data
@ApiModel("新增转账单数据对象")
public class AddAllotDTO   {

    /**
     * 所属组织
     */
    @NotBlank(message = "所属组织不能为空")
    @ApiModelProperty(value = "所属组织", example = "技术研发中心",required = true)
    private String frame;

    /**
     * 单据时间
     */
    @ApiModelProperty(value = "单据时间", example = "2025-10-27",required = true)
    private LocalDate time;

    /**
     * 单据编号
     */
    @NotBlank(message = "单据编号不能为空")
    @ApiModelProperty(value = "单据编号", example = "ALLOT20251027001",required = true)
    private String number;

    /**
     * 单据金额
     */
    @NotNull(message = "单据金额不能为空")
    @Min(value = 0, message = "金额不能小于0")
    @ApiModelProperty(value = "单据金额", example = "5680.30",required = true)
    private BigDecimal total;

    /**
     * 关联人员
     */

    @ApiModelProperty(value = "关联人员", example = "张三")
    private String people;

    /**
     * 单据附件
     */
    @ApiModelProperty(value = "单据附件", example = "/upload/allot/20251027/attachment.pdf")
    private String file;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "此单据为Q4季度设备分配单，需优先处理")
    private String data;

    /**
     * 扩展信息
     */
    @ApiModelProperty(value = "扩展信息", example = "{\"source\":\"ERP系统\",\"operatorType\":\"自动分配\"}")
    private String more;

    /**
     * 审核状态[0:未审核|1:已审核]
     */
    @Min(value = 0, message = "审核状态不能小于0")
    @Max(value = 1, message = "审核状态不能大于1")
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]", example = "0",required = true)
    private Integer examine;

    /**
     * 制单人
     */
    @NotBlank(message = "制单人不能为空")
    @ApiModelProperty(value = "制单人", example = "赵六",required = true)
    private String user;

    @ApiModelProperty(value = "转账单详情列表对象",required = true)
    List<AddAllotInfoDTO> allotInfoList;

}
