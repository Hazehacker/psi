package com.zeroone.star.storemanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * @author Coda
 * @date 2025/10/28
 * @description 日志表实体类
 */
@Data
@TableName("log")
public class LogDO {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    private LocalDateTime time;

    private String user;

    private String info;

}
