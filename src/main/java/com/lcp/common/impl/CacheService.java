package com.lcp.common.impl;


import com.lcp.common.ICacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Set;

@Component
@Slf4j
public class CacheService implements ICacheService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private SetOperations<String, Object> listOperations;

    @PostConstruct
    void init() {
        listOperations = redisTemplate.opsForSet();
    }

    @Override
    public Long add(String key, Object... values) {
        Long res = listOperations.add(key, values);
        if (res == null || res < 1) {
            log.error("Add fail [{}] - [{}], values already in cache or sth error.", key, values);
            return res;
        }
        log.info("Add success [{}]", key);
        return res;
    }

    @Override
    public Long add(String key, Date expireTime, Object... values) {
        Long res = listOperations.add(key, values);
        if (res == null || res < 1) {
            log.error("Add fail [{}] - [{}], values already in cache or sth error.", key, values);
            return res;
        }
        redisTemplate.expireAt(key, expireTime);
        log.info("Add success [{}], expires at [{}]", key, expireTime);
        return res;
    }

    @Override
    public Long clearAndAdd(String key, Object... values) {
        Set<Object> previousValues = listOperations.members(key);
        if (CollectionUtils.isNotEmpty(previousValues)) {
            previousValues.forEach(previousValue -> listOperations.remove(key, previousValue));
        }
        return add(key, values);
    }

    @Override
    public Boolean exist(String key, Object value) {
        return listOperations.isMember(key, value);
    }

    @Override
    public Set<Object> retrieveAll(String key) {
        return listOperations.members(key);
    }

    @Override
    public Boolean remove(String key) {
        Boolean res = redisTemplate.delete(key);
        log.info("Remove success [{}]", key);
        return res;
    }

    @Override
    public Long remove(String key, Object... values) {
        Long res = listOperations.remove(key, values);
        if (res == null || res < 1) {
            log.error("Remove fail [{}] - [{}], values already removed or sth error.", key, values);
            return res;
        }
        log.info("Remove success [{}]", key);
        return res;
    }

    @Override
    public Set<String> retrieveAllKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}
