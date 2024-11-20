package kz.nik.project01rediskeycloackwebsocket.service.impl;

import kz.nik.project01rediskeycloackwebsocket.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void cacheObject(String key, Object value, long timout, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, value, timout, timeUnit);
    }

    public Object getCachedObject(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteCachedObject(String key){
        redisTemplate.delete(key);
    }
}
