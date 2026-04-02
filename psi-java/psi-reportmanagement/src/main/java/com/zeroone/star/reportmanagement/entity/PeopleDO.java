package com.zeroone.star.reportmanagement.entity.salesreport;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 人员管理表
 * @author taohu5564
 * @date 2025/10/22
 */
@Getter
@Setter
@TableName("people")
public class PeopleDO {
    /*
    * 唯一id
    * */
    @TableId(value = "id")
    private Integer id;

    /*
    * 人员名称
    * */
    private String name;

    /*
    * 拼音信息
    * */
    private String py;

    /*
    * 人员编号
    * */
    private String number;

    /*
    * 所属组织
    * */
    private Integer frame;

    /*
    * 所属角色
    * */
    private Integer role;

    /*
    * 人员性别【0：女|1：男】
    * */
    private String sex;

    /*
    * 联系电话
    * */
    private String tel;

    /*
    * 联系地址
    * */
    private String add;

    /*
    * 身份证号
    * */
    private String card;

    /*
    * 备注信息
    * */
    private String data;

    /*
    * 扩展信息
    * */
    private String more;
}
