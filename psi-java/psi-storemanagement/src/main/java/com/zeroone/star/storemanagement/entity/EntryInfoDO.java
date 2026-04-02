package com.zeroone.star.storemanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "entry_info")
public class EntryInfoDO {

    private String id;

    private String pid;

    private String goods;

    private String attr;

    private String unit;

    private String warehouse;

    private String batch;

    private LocalDate mfd;

    private BigDecimal price;

    private BigDecimal nums;

    private String serial;

    private BigDecimal total;

    private String data;
}
