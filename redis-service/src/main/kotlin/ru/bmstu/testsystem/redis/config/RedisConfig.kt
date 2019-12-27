package ru.bmstu.testsystem.redis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisConfig {

    @Bean
    fun redisTemplate(): RedisTemplate<String?, Any?>? {
        val template: RedisTemplate<String?, Any?> = RedisTemplate()
        template.setConnectionFactory(jedisConnectionFactory())
        return template
    }

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        val jedisConFactory = JedisConnectionFactory()
        jedisConFactory.hostName = "localhost"
        jedisConFactory.port = 6379
        return jedisConFactory
    }
}