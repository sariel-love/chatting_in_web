package com.example.chatting_in_web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${redisson.address:redis://127.0.0.1:6379}")
    private String redisAddress;

    @Value("${redisson.password:}")
    private String redisPassword;

    @Value("${spring.redis.lettuce.pool.max-active:64}")
    private int connectionPoolSize;

    @Value("${spring.redis.lettuce.pool.max-active:10}")
    private int connectionMinimumIdleSize;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(ObjectMapper objectMapper) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisAddress)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize);

        if (redisPassword != null && !redisPassword.isEmpty()) {
            config.useSingleServer().setPassword(redisPassword);
        }

        // 使用 Jackson JSON 序列化（跨实例可读）
        config.setCodec(new JsonJacksonCodec(objectMapper));
        return Redisson.create(config);
    }
}
