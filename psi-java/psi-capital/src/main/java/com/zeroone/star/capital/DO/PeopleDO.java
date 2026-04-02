package com.zeroone.star.capital.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 人员管理数据对象
 * 用于管理企业内部人员的基本信息，包括人员名称、编号、联系方式等
 * 可用于各类业务单据中的人员关联，如付款单的经办人、制单人等
 * </p>
 *
 * @author Junjie
 * @since 2025-10-25
 */
@TableName("people")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleDO {
    /**
     * 人员唯一标识符
     * 主键，用于唯一标识一个人员
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @NotNull(message = "人员id不能为空")
    private String id;

    /**
     * 人员姓名
     * 人员的真实姓名
     */
    @NotNull(message = "人员名称不能为空")
    private String name;

    /**
     * 拼音信息
     * 人员姓名的拼音，用于快速检索和查询
     */
    @NotNull(message = "拼音信息不能为空")
    private String py;

    /**
     * 人员编号
     * 人员的工号或业务编号，用于业务标识
     */
    @NotNull(message = "人员编号不能为空")
    private String number;

    /**
     * 所属组织编号
     * 标识人员所属的部门或组织机构，默认值为'0'
     */
    @NotNull(message = "所属组织不能为空")
    private String frame;

    /**
     * 人员性别
     * 0-女
     * 1-男
     */
    @NotNull(message = "人员性别不能为空")
    private Integer sex;

    /**
     * 联系电话
     * 人员的手机号码或办公电话
     */
    @NotNull(message = "联系电话不能为空")
    private String tel;

    /**
     * 联系地址
     * 人员的家庭地址或办公地址
     */
    private String add;

    /**
     * 身份证号
     * 人员的身份证号码，用于实名认证
     */
    private String card;

    /**
     * 备注信息
     * 记录人员的补充说明或特殊备注
     */
    private String data;

    /**
     * 扩展信息
     * 以JSON或其他格式存储的扩展字段，用于存储额外信息
     */
    private String more;
}
