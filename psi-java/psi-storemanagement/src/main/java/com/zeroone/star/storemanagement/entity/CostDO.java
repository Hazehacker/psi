package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CostDO {

  private String id;

  private String type;

  @TableField("class")
  private String cls;

  private LocalDateTime time;

  private String iet;

  private BigDecimal money;

  private String data;

  private BigDecimal settle;

  private Integer state;


}
