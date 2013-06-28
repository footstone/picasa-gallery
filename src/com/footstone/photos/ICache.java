package com.footstone.photos;

public interface ICache {
	
	public void save(String key,byte[] data) throws Exception;
	
	public byte[] get(String key) throws Exception;

}
