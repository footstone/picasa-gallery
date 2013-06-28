package com.footstone.photos;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.azib.photos.Picasa;

import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;

/**
 * key-url cache
 * 
 * @author footstone
 *
 */
public class PhotoURLCache {
	private static final Logger log = Logger.getLogger(PhotoURLCache.class.getName());
	private static Map<String,String> cache = Collections.synchronizedMap(new HashMap<String,String>());
	
	public static void main(String[] args) {
		load();
	}
	
	public static void load(){
		long start_time = System.currentTimeMillis();
		Picasa picasa = new Picasa(ConfigFactory.getUserName(),null);
		// test
        UserFeed gallery = picasa.getGallery();
        List<AlbumEntry> albums = gallery.getAlbumEntries();
        for (AlbumEntry album : albums){
        	String albumName = album.getName();
        	String remotePath = album.getMediaThumbnails().get(0).getUrl();
    		String mediaKey = Util.getKey(albumName, remotePath);
    		cache.put(mediaKey, remotePath);
    		
	    	AlbumFeed af = picasa.getAlbum(album.getName());
	    	
	    	List<PhotoEntry> photos = af.getPhotoEntries();
	    	for (PhotoEntry photo : photos){
	    		String thumbnailUrl = photo.getMediaThumbnails().get(0).getUrl();
	    		String thumbnailKey = Util.getKey(albumName, thumbnailUrl);
	    		cache.put(thumbnailKey, thumbnailUrl);
	    		
	    		String contentUrl = photo.getMediaContents().get(0).getUrl();
	    		String contentKey = Util.getKey(albumName, contentUrl);
	    		cache.put(contentKey, contentUrl);
	    	}
        }
        long end_time = System.currentTimeMillis();
        
        System.out.println((end_time-start_time)/1000+"s");
        
        log.severe("---------------------------");
        log.severe("photos url cached in "+(end_time-start_time)/1000+"s");
        log.severe("---------------------------");
        
	}
	
	public static void put(String key,String url){
		if (key == null || url == null){
			return;
		}
		if (cache.containsKey(key)){
			return;
		}
		cache.put(key, url);
	}
	
	public static String get(String key){
		return cache.get(key);
	}

	
}
