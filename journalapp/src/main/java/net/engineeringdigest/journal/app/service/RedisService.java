package net.engineeringdigest.journal.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if(o != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(o.toString(), entityClass);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("error while getting key: {}", key, e);
            return null;
        }
    }

    public void set(String key, Object o, Long ttl) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o);
             redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("error while setting key: {} and object: {}", key, o, e);
        }
    }

}
