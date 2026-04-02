package com.zeroone.star.finance.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 收支类别
 * </p>
 *
 * @author author
 * @since 2025-11-01
 */
@Getter
@Setter
@TableName("iet")
public class Iet implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("类别ID")
    private String id;

    @ExcelProperty("类别名称")
    private String name;

    @ExcelProperty("收支类型")
    private Integer type;

    @ExcelProperty("类别排序")
    private Integer sort;

    @ExcelProperty("备注信息")
    private String data;
}