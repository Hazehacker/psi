package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("goods")
public class GoodsDO {

    @TableId(value = "id",type = IdType.NONE)
    private String id;

    private String name;

    private String py;

    private String number;

    private String spec;

    private String category;

    private String brand;

    private String unit;

    private BigDecimal buy;

    private BigDecimal sell;

    private String code;

    private String location;

    private BigDecimal stock;

    private Integer type;

    private String data;

    private String imgs;

    private String details;

    private String units;

    private String strategy;

    private Integer serial;

    private Integer batch;

    private Integer validity;

    private Integer protect;

    private Integer threshold;

    private String more;

    //给不能为空但是插入时dto没有的字段设置默认值
    public void setValue() {
        this.py = "根据商品名称生成";
        this.category = "默认商品类别";
        this.unit = "默认商品单位";
        this.buy = BigDecimal.ZERO;
        this.sell = BigDecimal.ZERO;
        this.stock = BigDecimal.ZERO;
        this.type = 0;
        this.more = "默认扩展信息";
    }
}
