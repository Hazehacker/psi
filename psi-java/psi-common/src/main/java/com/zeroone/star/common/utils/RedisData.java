package com.zeroone.star.common.utils;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Redis 逻辑过期数据包装类
 * 用于缓存击穿的逻辑过期方案
 */
@Data
public class RedisData {
    private Object data;
    private LocalDateTime expireTime;
}
