package eu.apenet.commons.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Cache<K,T>  {
	/** Objects are stored here */
	private final Map<K, T> objects;
	/** Holds custom expiration dates */
	private final Map<K, Long> expire;
	/** The default expiration date */
	private final long defaultExpire;
	/** Is used to speed up some operations */
	private final ExecutorService threads;

	/**
	 * Constructs the cache with a default expiration time for the objects of
	 * 5 minutes.
	 */
	public Cache() {
		this(300);
	}

	/**
	 * Construct a cache with a custom expiration date for the objects.
	 * 
	 * @param defaultExpire
	 *            default expiration time in seconds
	 */
	public Cache(final long defaultExpire) {
		this.objects = Collections.synchronizedMap(new HashMap<K, T>());
		this.expire = Collections.synchronizedMap(new HashMap<K, Long>());

		this.defaultExpire = defaultExpire;

		this.threads = Executors.newFixedThreadPool(256);
		Executors.newScheduledThreadPool(2).scheduleWithFixedDelay(this.removeExpired(), this.defaultExpire / 2, this.defaultExpire, TimeUnit.SECONDS);
	}

	/**
	 * This Runnable removes expired objects.
	 */
	private final Runnable removeExpired() {
		return new Runnable() {
			public void run() {
				for (final K name : expire.keySet()) {
					if (System.currentTimeMillis() > expire.get(name)) {
						threads.execute(createRemoveRunnable(name));
					}
				}
			}
		};
	}

	/**
	 * Returns a runnable that removes a specific object from the cache.
	 * 
	 * @param name
	 *            the name of the object
	 */
	private final Runnable createRemoveRunnable(final K name) {
		return new Runnable() {
			public void run() {
				objects.remove(name);
				expire.remove(name);
			}
		};
	}

	/**
	 * Returns the default expiration time for the objects in the cache.
	 * 
	 * @return default expiration time in seconds
	 */
	public long getExpire() {
		return this.defaultExpire;
	}

	/**
	 * Put an object into the cache.
	 * 
	 * @param name
	 *            the object will be referenced with this name in the cache
	 * @param obj
	 *            the object
	 */
	public void put(final K name, final T obj) {
		this.put(name, obj, this.defaultExpire);
	}

	/**
	 * Put an object into the cache with a custom expiration date.
	 * 
	 * @param name
	 *            the object will be referenced with this name in the cache
	 * @param obj
	 *            the object
	 * @param expire
	 *            custom expiration time in seconds
	 */
	private void put(final K name, final T obj, final long expireTime) {
		this.objects.put(name, obj);
		this.expire.put(name, System.currentTimeMillis() + expireTime * 1000);
	}

	/**
	 * Returns an object from the cache.
	 * 
	 * @param name
	 *            the name of the object you'd like to get
	 * @param type
	 *            the type of the object you'd like to get
	 * @return the object for the given name and type
	 */
	public T get(final K name) {
		final Long expireTime = this.expire.get(name);
		if (expireTime == null) return null;
		if (System.currentTimeMillis() > expireTime) {
			this.threads.execute(this.createRemoveRunnable(name));
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


}
