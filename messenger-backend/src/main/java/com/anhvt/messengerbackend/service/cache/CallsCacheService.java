/**
 * Copyright 2024
 * Name: CallsCacheService
 */
package com.anhvt.messengerbackend.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Service
public class CallsCacheService {

    @Autowired
    private CacheManager cacheManager;

    private Cache getCache() {
        return cacheManager.getCache("calls");
    }

    public Object getAll() {
        return this.getCache();
    }

    public void setUser(String callUrl, int userId) {
        Cache cache = this.getCache();
        Cache.ValueWrapper value = cache.get(callUrl);
        if (value != null && value.get() != null) {
            ArrayList<Integer> userIds = (ArrayList<Integer>) value.get();
            assert userIds != null;
            if (!userIds.contains(userId)) {
                userIds.add(userId);
                cache.put(callUrl, userIds);
            }
        } else {
            cache.put(callUrl, new ArrayList<>(List.of(userId)));
        }
    }

    public boolean removeUser(String callUrl, int userId) {
        Cache cache = this.getCache();
        Cache.ValueWrapper value = cache.get(callUrl);
        if (value != null && value.get() != null) {
            ArrayList<Integer> userIds = (ArrayList<Integer>) value.get();
            assert userIds != null;
            userIds.remove(userId);
            return userIds.isEmpty();
        }
        return false;
    }
}
