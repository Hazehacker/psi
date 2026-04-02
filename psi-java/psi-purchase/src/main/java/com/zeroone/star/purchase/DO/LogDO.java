package com.zeroone.star.purchase.DO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
* 操作日志
* @TableName log
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("log")
public class LogDO implements Serializable {

    /**
    * 
    */
    @NotBlank(message="[]不能为空")
    @ApiModelProperty("操作ID")
    @Length(max= 32,message="编码长度不能超过32")
    @TableId(type = IdType.INPUT)
    private String id;
    /**
    * 操作时间
    */
    @NotNull(message="[操作时间]不能为空")
    @ApiModelProperty("操作时间")
    private LocalDateTime time;
    /**
    * 所属用户
    */
    @NotBlank(message="[所属用户]不能为空")
    @ApiModelProperty("所属用户")
    @Length(max= 32,message="编码长度不能超过32")
    private String user;
    /**
    * 操作内容
    */
    @NotBlank(message="[操作内容]不能为空")
    @ApiModelProperty("操作内容")
    @Length(max= 256,message="编码长度不能超过256")
    private String info;



}
