package com.util.background;

import com.util.func.cache.CacheService;

public class ClearCacheJob implements Runnable {
    @Override
    public void run() {
        try {
            CacheService.getInstance().clear();
        } catch (Exception e) {
        }
    }
}
