package com.example.smsapplication.services.redis;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisRateLimiter {

    @Autowired
    private StringRedisTemplate stringTemplate;

    private static final int REQUESTS_PER_24_HOURS = 50;

    public boolean isAllowed(String phoneNumber) {
        final int hour = LocalDateTime.now().getHour();
        String key = phoneNumber + ":" + hour;
        ValueOperations<String, String> operations = stringTemplate.opsForValue();
        String requests = operations.get(key);
        if(StringUtils.isNotBlank(requests) && Integer.parseInt(requests) >= REQUESTS_PER_24_HOURS) return false;

        List<Object> transactionResults = stringTemplate.execute(new SessionCallback<List<Object>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <K,V> List<Object> execute(final RedisOperations<K,V> operations) throws DataAccessException {
                final StringRedisTemplate redisTemplate = (StringRedisTemplate) operations;
                final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
                operations.multi();
                valueOperations.increment(key);
                redisTemplate.expire(key, 24, TimeUnit.HOURS);

                return operations.exec();
            }
            });

        return true;
    }
    public boolean isLimitExceeded(String key){
        return isAllowed(key);
    }
}
