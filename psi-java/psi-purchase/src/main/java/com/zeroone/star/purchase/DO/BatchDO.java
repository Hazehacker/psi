package com.zeroone.star.purchase.DO;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: 小王
 * @description: 批次号实体
 * @create: 2025-10-24
 * @Version: 1.0
 **/
@Data
@ApiModel("批次号实体")
@TableName("batch")
@AllArgsConstructor
@NoArgsConstructor
public class BatchDO {

    @ApiModelProperty(value = "所属商品",example = "铅笔")
    private String goods;

    @ApiModelProperty(value = "id",example = "1")
    private String id;

    @ApiModelProperty(value = "批次号",example = "10009")
    private String number;

    @ApiModelProperty(value = "库存数量",example = "100")
    private BigDecimal nums;

    @ApiModelProperty(value = "所属仓储",example = "01")
    private String room;

    @ApiModelProperty(value = "生产日期",example = "2025-10-1")
    private LocalDateTime time;

    @ApiModelProperty(value = "所属仓库",example = "90")
    private String warehouse;
}
