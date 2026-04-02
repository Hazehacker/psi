package com.zeroone.star.project.dto.j7.money.transfer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 类名：AllotDeatilDTO
 * 包名：com.zeroone.star.project.dto.j7.money.transfer
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/31
 * 版本号：V1.0
 */
@Data
@ApiModel("获取指定转账单详情数据对象")
public class AllotDetailDTO {


    @ApiModelProperty(value = "转账单id",example = "1")
    private String id;

    /**
     * 单据时间
     */
    @ApiModelProperty(value = "单据时间", example = "2025-10-27")
    private LocalDate time;

    /**
     * 单据编号
     */

    @ApiModelProperty(value = "单据编号", example = "ALLOT20251027001")
    private String number;

    /**
     * 单据金额
     */


    @ApiModelProperty(value = "单据金额", example = "5680.30")
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
    List<AllotInfoDTO> allotInfoDTOList;
}
