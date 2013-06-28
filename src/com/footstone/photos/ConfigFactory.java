package com.footstone.photos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigFactory {
	private static final Logger log = Logger.getLogger(ConfigFactory.class.getName());
	private static String userName;
	private static ICache cache;
	private static String analytics;
	private static List<String> imageList = new ArrayList<String>();
	private static String localPath = "";
	static{
		Properties prop = new Properties();
		try {
			prop.load(ConfigFactory.class.getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			log.severe("load config.properties error!"+e.getMessage());
		}
		Object obj = prop.get("google.user");
		if (obj != null){
			userName = String.valueOf(obj);
		}
//		String implStr = prop.getProperty("cache.impl", "com.footstone.photos.GoogleCacheImpl");
//		try {
//			cache = (ICache)Class.forName(implStr).newInstance();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		analytics = prop.getProperty("google.analytics");
		
		String urlStr = prop.getProperty("image.url");
		
		if (urlStr!=null && !("").equals(urlStr)){
			String[] urls = urlStr.split(";");
			imageList = Arrays.asList(urls);
		}
		
		localPath = prop.getProperty("local.path");
		
	}
	
	public static String getUserName(){
		return userName;
	}
	public static ICache getCache(){
		return cache;
	}
	public static String getAnalytics(){
		return analytics;
	}
	public static List<String> getImageList(){
		return imageList;
	}
	public static String getLocalPath(){
		return localPath;
	}
}
