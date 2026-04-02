package com.zeroone.star.capital.DO;

import javax.validation.constraints.*;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 平生
* 其它支出单核销详情
* @TableName oce_bill
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("oce_bill")
public class OceBillDO implements Serializable {

    /**
    * id
    */
    @NotBlank(message="[]不能为空")
    @ApiModelProperty("")
    @Length(max= 32,message="编码长度不能超过32")
    private String id;
    /**
    * 所属单据
    */
    @NotBlank(message="[所属单据]不能为空")
    @ApiModelProperty("所属单据")
    @Length(max= 32,message="编码长度不能超过32")
    private String pid;
    /**
    * 核销类型
    */
    @NotBlank(message="[核销类型]不能为空")
    @ApiModelProperty("核销类型")
    @Length(max= 32,message="编码长度不能超过32")
    private String type;
    /**
    * 关联单据
    */
    @NotBlank(message="[关联单据]不能为空")
    @ApiModelProperty("关联单据")
    @Length(max= 32,message="编码长度不能超过32")
    private String source;
    /**
    * 单据时间
    */
    @NotNull(message="[单据时间]不能为空")
    @ApiModelProperty("单据时间")
    private LocalDateTime time;
    /**
    * 核销金额
    */
    @NotNull(message = "[核销金额]不能为空")
    @Digits(integer = 16, fraction = 4, message = "金额格式不正确")
    @DecimalMin(value = "0", inclusive = true, message = "金额不能为负数")
    @ApiModelProperty("核销金额")
    private BigDecimal money;


}
