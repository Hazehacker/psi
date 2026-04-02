package com.zeroone.star.project.dto.j3.purchase;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author TWTW
 * @date 2025/10/20 16:22
 * @description 采购退货单数据传输对象，用于采购单相关的参数传递与响应
 * Version: 1.0
 */
@Data
@ApiModel(description = "采购退货单数据传输对象")

public class PurchaseNoteBuildDTO {
    // 返回信息包含采购单核心信息、采购单明细列表
    @ApiModelProperty(value = "采购单核心信息")
    private PurchaseNoteBorDTO purchaseNoteBorDTO;
    @ApiModelProperty(value = "采购单明细列表")
    private List<PurchaseNoteBuilInfoDTO> purchaseNoteBuilInfoDTOS;

}