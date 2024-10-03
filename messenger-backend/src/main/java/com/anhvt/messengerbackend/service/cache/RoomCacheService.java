/**
 * Copyright 2024
 * Name: RoomCacheService
 */
package com.anhvt.messengerbackend.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Service
public class RoomCacheService {

    @Autowired
    private CacheManager cacheManager;

    private Cache getCache() {
        return cacheManager.getCache("rooms");
    }

    public void setNewRoom(String roomUrl, Object object) {
        Cache roomsCache = this.getCache();
        roomsCache.put(roomUrl, object);
    }

    public List<String> getAllKeys() {
        ConcurrentHashMap<String, HashMap<String, ArrayList<Integer>>> map =
                (ConcurrentHashMap) this.getCache().getNativeCache();
        return Collections.list(map.keys());
    }

    public Object getRoomByKey(String groupUrl) {
        Cache roomsCache = this.getCache();
        if (roomsCache != null) {
            Cache.ValueWrapper valueWrapper = roomsCache.get(groupUrl);
            if (valueWrapper != null) {
                return valueWrapper.get();
            }
        }
        return null;
    }
}