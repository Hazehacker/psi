package com.zeroone.star.basic_information.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Frame implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 父级组织
     */
    private String pid;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 排列顺序
     */
    private Integer sort;

    /**
     * 备注信息
     */
    private String data;
}
