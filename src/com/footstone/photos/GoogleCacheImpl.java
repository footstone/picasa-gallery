package com.footstone.photos;

import java.util.Collections;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

public class GoogleCacheImpl implements ICache {
	
	private static final Logger log = Logger.getLogger(GoogleCacheImpl.class.getName());
	private static Cache cache = null;
	
	static{
		try {
    		cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
    	} catch (CacheException e) {
    		log.severe("JCache init failed!"+e.getMessage());
    	}
	}

	public void save(String key, byte[] data) throws Exception {
		if (key == null){
			return ;
		}
    	cache.put(key, data);
	}

	public byte[] get(String key) throws Exception {
		if (cache.containsKey(key)){
			return (byte[])cache.get(key);
		}
		return null;
	}

}
