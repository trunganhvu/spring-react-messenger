/**
 * Copyright 2024
 * Name: CacheConfig
 */
package com.anhvt.messengerbackend.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("rooms", "calls");
    }
}
