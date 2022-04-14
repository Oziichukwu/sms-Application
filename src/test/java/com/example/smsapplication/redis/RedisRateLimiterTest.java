package com.example.smsapplication.redis;

import com.example.smsapplication.services.redis.RedisRateLimiter;
import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class RedisRateLimiterTest {

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisRateLimiter redisRateLimiter;

    @Autowired
    public RedisRateLimiterTest(RedisTemplate<String, String> redisTemplate,
                                RedisRateLimiter redisRateLimiter) {
        this.redisTemplate = redisTemplate;
        this.redisRateLimiter = redisRateLimiter;
    }

    @Test
    @DisplayName("Method return true when rate limiter is exceeded Test")
    void test_returnsTrueWhenRateLimiterIsExceededTest(){

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("Micheal", "80");
        boolean result = redisRateLimiter.isLimitExceeded("Micheal");
        assertTrue(result);
    }

//    @Test
//    @DisplayName("Method returns false when rate limiter is not exceeded Test")
//    void test_returnsFalseWhenRateLimiterIsNotExceededTest(){
//        ValueOperations<String, String> operations = redisTemplate.opsForValue();
//        operations.set("Micheal", "10");
//        boolean result = redisRateLimiter.isLimitExceeded("Micheal");
//        assertFalse(result);
//    }
}
