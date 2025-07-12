package com.jason.community.mvc.service.impl;

import com.jason.community.common.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 操作 Redis数据库，用于读写验证码
 *
 * @author Jason
 * @version 1.0
 */
@Service
public class RedisService {

    @Autowired // redis操作对象
    private StringRedisTemplate redisTemplate;

    /**
     * 设置键值对
     */
    public ResultEntity<String> setRedisKeyValue(String key, String value) {
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 设置键值对（带失效时间）
     *
     * @param key      键
     * @param value    值
     * @param time     超时时间
     * @param timeUnit 时间单位
     */
    public ResultEntity<String> setRedisKeyValueWithTimeout(String key, String value, Long time, TimeUnit timeUnit) {
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value, time, timeUnit);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 key，获取 Value
     */
    public ResultEntity<String> getRedisValue(String key) {
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String value = operations.get(key);

            return ResultEntity.successWithData(value);
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除 key
     */
    public ResultEntity<String> delRedisKey(String key) {
        try {
            redisTemplate.delete(key);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }
}
