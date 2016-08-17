package eu.apenet.commons.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Cache<K, T> {

    /**
     * Objects are stored here
     */
    private final Map<K, T> objects;
    /**
     * Holds custom expiration dates
     */
    private final Map<K, Long> expire;
    /**
     * The default expiration date
     */
    private final long defaultExpire;

    private String name;

    /**
     * Construct a cache with a custom expiration date for the objects.
     *
     * @param defaultExpire default expiration time in seconds
     */
    protected Cache(final long defaultExpire, String name) {
        this.objects = Collections.synchronizedMap(new HashMap<K, T>());
        this.expire = Collections.synchronizedMap(new HashMap<K, Long>());

        this.defaultExpire = defaultExpire;
        this.name = name;

    }

    /**
     * Put an object into the cache.
     *
     * @param name the object will be referenced with this name in the cache
     * @param obj the object
     */
    public void put(final K name, final T obj) {
        this.put(name, obj, this.defaultExpire);
    }

    /**
     * Put an object into the cache with a custom expiration date.
     *
     * @param name the object will be referenced with this name in the cache
     * @param obj the object
     * @param expire custom expiration time in seconds
     */
    private void put(final K name, final T obj, final long expireTime) {
        this.objects.put(name, obj);
        this.expire.put(name, System.currentTimeMillis() + expireTime * 1000);
    }

    /**
     * Returns an object from the cache.
     *
     * @param name the name of the object you'd like to get
     * @param type the type of the object you'd like to get
     * @return the object for the given name and type
     */
    public T get(final K name) {
        final Long expireTime = this.expire.get(name);
        if (expireTime == null) {
            return null;
        }
        if (System.currentTimeMillis() > expireTime) {
            return null;
        }
        return this.objects.get(name);
    }

    /**
     * Convenience method.
     */
    @SuppressWarnings("unchecked")
    public <R extends T> R get(final K name, final Class<R> type) {
        return (R) this.get(name);
    }

    protected Map<K, T> getObjects() {
        return objects;
    }

    protected Map<K, Long> getExpire() {
        return expire;
    }

    protected String getName() {
        return name;
    }

}
