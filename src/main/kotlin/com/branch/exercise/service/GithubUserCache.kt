package com.branch.exercise.service

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class GithubUserCache(@Value("\${user.service.cache.expire.minutes}") private val expireMinutes: Long,
                     @Value("\${user.service.cache.limit}") private val cacheLimit: Long) {

    @Bean
    fun caffeineConfig(): Caffeine<Any, Any> = Caffeine
            .newBuilder()
            .maximumSize(cacheLimit)
            .expireAfterWrite(expireMinutes, TimeUnit.MINUTES)

    @Bean
    fun cacheManager(caffeine: Caffeine<Any, Any>): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()
        caffeineCacheManager.setCaffeine(caffeine)
        return caffeineCacheManager
    }
}