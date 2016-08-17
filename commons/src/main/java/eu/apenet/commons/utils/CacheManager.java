package eu.apenet.commons.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class CacheManager {

    private static final Logger LOGGER = Logger.getLogger(CacheManager.class);
    private static CacheManager instance;
    private List<Cache<?, ?>> caches = new ArrayList<Cache<?, ?>>();
    private static final long DEFAULT_EXPIRE = 3600;

    private CacheManager() {
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(this.removeExpired(), DEFAULT_EXPIRE / 2, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    public <K, T> Cache<K, T> initCache(String name) {
        LOGGER.info("Add cache " + name + " to " + this.toString());
        for (Cache<?, ?> cache : caches) {
            if (name.equals(cache.getName())) {
                throw new RuntimeException("There is already a cache with the same name: " + name);
            }
        }
        Cache<K, T> cache = new Cache<K, T>(DEFAULT_EXPIRE, name);
        caches.add(cache);
        return cache;
    }

    /**
     * This Runnable removes expired objects.
     */
    private final Runnable removeExpired() {
        return new Runnable() {
            public void run() {
                LOGGER.debug("Start cleaning caches");
                for (Cache<?, ?> cache : caches) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Start cleaning cache:" + cache.getObjects().size() + " " + cache.getName());
                    }
                    List<Object> keys = new ArrayList<Object>();
                    for (Object expireKeys : cache.getExpire().keySet()) {
                        if (System.currentTimeMillis() > cache.getExpire().get(expireKeys)) {
                            keys.add(expireKeys);
                        }
                    }
                    for (Object key : keys) {
                        cache.getExpire().remove(key);
                        cache.getObjects().remove(key);
                    }
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Stop cleaning cache:" + cache.getObjects().size());
                    }
                }
                LOGGER.debug("Stop cleaning caches");

            }
        };
    }
}
