package kz.nik.project01rediskeycloackwebsocket.service;

import kz.nik.project01rediskeycloackwebsocket.service.impl.CacheServiceImpl;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public interface CacheService {

    void cacheObject(String key, Object value, long timout, TimeUnit timeUnit);

    Object getCachedObject(String key);

    void deleteCachedObject(String key);
}
