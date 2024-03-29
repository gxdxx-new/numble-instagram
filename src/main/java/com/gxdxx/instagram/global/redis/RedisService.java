package com.gxdxx.instagram.global.redis;

import com.gxdxx.instagram.global.auth.exception.RefreshTokenInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    public static final String REFRESH_TOKEN_KEY_PREFIX = "RefreshToken:";
    public static final String ACCESS_TOKEN_BLACKLIST_VALUE = "logout";

    public void setValues(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, long duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration, TimeUnit.MILLISECONDS);
    }

    public String getValues(String key) {
        if (!keyExists(key)) {
            return null;
        }
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public boolean keyExists(String key) {
        return redisTemplate.hasKey(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void checkRefreshToken(Long userId, String refreshToken) {
        String redisRT = this.getValues(REFRESH_TOKEN_KEY_PREFIX + userId);
        if(!refreshToken.equals(redisRT)) {
            throw new RefreshTokenInvalidException();
        }
    }

}
