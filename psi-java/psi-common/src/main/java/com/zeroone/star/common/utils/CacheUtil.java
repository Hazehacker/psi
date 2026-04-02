package com.zeroone.star.common.utils;

import cn.hutool.json.JSONUtil;
import com.zeroone.star.common.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 缓存工具类
 * <p>
 * 提供缓存穿透、缓存击穿、缓存雪崩的防御措施
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheUtil {
    private final StringRedisTemplate stringRedisTemplate;

    // ==================== 写入操作 ====================

    /**
     * 将任意java对象序列化为json并存储在string类型的key中，
     * 并且设置随机过期时间（防止缓存雪崩）
     *
     * @param key   缓存键
     * @param value 缓存值
     * @param time  基础过期时间
     * @param unit  时间单位
     */
    public void setWithRandomExpire(String key, Object value, Long time, TimeUnit unit) {
        // 在基础过期时间上加上随机偏移量，防止缓存雪崩
        long randomOffset = ThreadLocalRandom.current().nextLong(CacheConstants.RANDOM_TTL_MAX);
        long finalTime = time + randomOffset;
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), finalTime, unit);
        log.debug("缓存写入，key={}, ttl={}{}", key, finalTime, unit);
    }

    /**
     * 将任意java对象序列化为json并存储在string类型的key中，
     * 并且可以设置逻辑过期时间（用于缓存击穿方案）
     *
     * @param key  缓存键
     * @param value 缓存值
     * @param time  过期时间
     * @param unit  时间单位
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
        log.debug("缓存写入（逻辑过期），key={}, expireTime={}", key, redisData.getExpireTime());
    }

    /**
     * 基本写入（固定过期时间）
     *
     * @param key   缓存键
     * @param value 缓存值
     * @param time  过期时间
     * @param unit  时间单位
     */
    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    // ==================== 查询操作（带防御逻辑） ====================

    /**
     * 获取缓存（附带应对缓存穿透问题的逻辑）
     * <p>
     * 缓存穿透：大量请求访问不存在的数据，导致请求直接打到数据库
     * 防御方案：将空值也写入缓存，防止同一key的穿透请求打到数据库
     * </p>
     *
     * @param keyPrefix  key前缀
     * @param id         业务id
     * @param type       返回类型
     * @param dbFallback 数据库查询函数
     * @param time       过期时间
     * @param unit       时间单位
     * @return 查询结果
     */
    public <T, ID> T queryWithPassThrough(String keyPrefix, ID id, Class<T> type,
                                          Function<ID, T> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;
        return queryWithPassThroughInternal(key, type, () -> dbFallback.apply(id), time, unit);
    }

    /**
     * 获取缓存（直接使用完整key，带缓存穿透防御）
     *
     * @param key        完整缓存key
     * @param type       返回类型
     * @param dbFallback 数据库查询函数（无参数）
     * @param time       过期时间
     * @param unit       时间单位
     * @return 查询结果
     */
    public <T> T queryWithPassThrough(String key, Class<T> type,
                                      java.util.function.Supplier<T> dbFallback,
                                      Long time, TimeUnit unit) {
        return queryWithPassThroughInternal(key, type, dbFallback, time, unit);
    }

    private <T> T queryWithPassThroughInternal(String key, Class<T> type,
                                              java.util.function.Supplier<T> dbFallback,
                                              Long time, TimeUnit unit) {
        String json = stringRedisTemplate.opsForValue().get(key);

        // 判断命中的是否是空值[应对缓存穿透]
        if ("".equals(json)) {
            return null;
        }
        if (json != null) {
            // 存在，直接返回
            return JSONUtil.toBean(json, type);
        }

        // 缓存中不存在，进行缓存重建
        T t = dbFallback.get();

        // 数据库不存在数据
        if (t == null) {
            // 将空值写入redis，防止缓存穿透
            stringRedisTemplate.opsForValue().set(key, "");
            return null;
        }
        // 数据库存在数据，写入redis
        setWithRandomExpire(key, t, time, unit);

        return t;
    }

    /**
     * 获取缓存（附带应对缓存击穿问题的逻辑）【互斥锁方案】
     * <p>
     * 缓存击穿：热点数据缓存过期瞬间，大量并发请求直接打到数据库
     * 防御方案：使用互斥锁，保证只有一个线程重建缓存，其他线程等待
     * </p>
     *
     * @param keyPrefix  key前缀
     * @param id         业务id
     * @param type       返回类型
     * @param dbFallback 数据库查询函数
     * @param time       过期时间
     * @param unit       时间单位
     * @return 查询结果
     * @throws InterruptedException 如果等待锁时被中断
     */
    public <T, ID> T queryWithMutex(String keyPrefix, ID id, Class<T> type,
                                    Function<ID, T> dbFallback, Long time, TimeUnit unit) throws InterruptedException {
        String key = keyPrefix + id;
        return queryWithMutexInternal(key, type, () -> dbFallback.apply(id), time, unit);
    }

    /**
     * 获取缓存（直接使用完整key，带缓存击穿防御）【互斥锁方案】
     *
     * @param key        完整缓存key
     * @param type       返回类型
     * @param dbFallback 数据库查询函数（无参数）
     * @param time       过期时间
     * @param unit       时间单位
     * @return 查询结果
     * @throws InterruptedException 如果等待锁时被中断
     */
    public <T> T queryWithMutex(String key, Class<T> type,
                                java.util.function.Supplier<T> dbFallback,
                                Long time, TimeUnit unit) throws InterruptedException {
        return queryWithMutexInternal(key, type, dbFallback, time, unit);
    }

    private <T> T queryWithMutexInternal(String key, Class<T> type,
                                        java.util.function.Supplier<T> dbFallback,
                                        Long time, TimeUnit unit) throws InterruptedException {
        String json = stringRedisTemplate.opsForValue().get(key);

        // 判断命中的是否是空值
        if ("".equals(json)) {
            return null;
        }
        if (json != null) {
            // 存在，直接返回
            return JSONUtil.toBean(json, type);
        }

        // 缓存中不存在，下面进行缓存重建
        // a. 尝试获取互斥锁
        String lockKey = CacheConstants.LOCK_PREFIX + key;
        boolean isLock = tryLock(lockKey);

        // b. 判断是否获取到锁
        while (!isLock) {
            // c. 没获取到锁，休眠一段时间后再尝试从缓存获取
            Thread.sleep(50);
            json = stringRedisTemplate.opsForValue().get(key);
            if (json != null) {
                return JSONUtil.toBean(json, type);
            }
            isLock = tryLock(lockKey);
        }

        // d. 获取到锁，从数据库查询数据，并写入缓存；最后释放互斥锁
        try {
            // 再次检查缓存（doublecheck，可能其他线程已经重建）
            json = stringRedisTemplate.opsForValue().get(key);
            if (json != null) {
                return JSONUtil.toBean(json, type);
            }

            T t = dbFallback.get();
            if (t == null) {
                // 数据库不存在数据，防止缓存穿透
                stringRedisTemplate.opsForValue().set(key, "");
                return null;
            }
            setWithRandomExpire(key, t, time, unit);
            return t;
        } finally {
            // 释放互斥锁
            unlock(lockKey);
        }
    }

    // ==================== 锁操作 ====================

    /**
     * 尝试获取锁
     *
     * @param key 锁键
     * @return 是否获取成功
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(flag);
    }

    /**
     * 释放锁
     *
     * @param key 锁键
     */
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    // ==================== 删除操作 ====================

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 删除缓存（带前缀匹配）
     * 注意：生产环境中建议使用 SCAN 命令替代 KEYS 命令避免阻塞
     *
     * @param keyPrefix 前缀
     */
    public void deleteWithPrefix(String keyPrefix) {
        Set<String> keys = stringRedisTemplate.keys(keyPrefix + "*");
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }
}
