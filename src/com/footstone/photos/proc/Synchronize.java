package com.footstone.photos.proc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import net.azib.photos.Picasa;

import com.footstone.photos.ConfigFactory;
import com.footstone.photos.Util;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;

public class Synchronize {
	
	private static String CACHE_DIR = ConfigFactory.getLocalPath();
	public static final String THUMBNAIL_DIR_NAME = "s144-c";
	public static final String CONTENT_DIR_NAME = "s1600";
	public static final String SMALL_DIR_NAME = "s212-c";
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args == null || args.length != 1){
			System.out.println("process must have a param!");
			System.exit(0);
			//throw new Exception("process must have a param");
		}
		
		long start_time = System.currentTimeMillis();
		
		String action = args[0];
		if (action.equalsIgnoreCase("sync")){
			sync();
		}else{
			//TODO
			
		}
		
		long end_time = System.currentTimeMillis();
	
		System.out.println("finished. cost:" + (end_time - start_time)/1000 + "s");
	}
	
	/**
	 * synchronize
	 * 
	 * @throws IOException
	 */
	public static void sync() throws IOException {
		List<String> list = getUnCachedAlbumNames();
		System.out.println("------------------------------");
		
		if (list == null || list.size()==0){
			System.out.println("synchronized.");
			System.out.println("------------------------------");
			return;
		}
		
		System.out.println("unsynchronized:");
		System.out.println();
		for (String str:list){
			System.out.println(str);
		}
		
		System.out.println("------------------------------");
		System.out.println();
		System.out.println("synchronizing...");
		
		for (String aName:list){
			downloadAlbum(aName);
			System.out.println("album:"+aName+" finished.");
		}
	}
	
	/**
	 * get uncached album names
	 * @param userName
	 * @return
	 * @throws IOException
	 */
	public static List<String> getUnCachedAlbumNames() {
		List<String> unCachedAlbumNames = new ArrayList<String>();
		
		// get albums that doesn't local cached
		File localDir = new File(ConfigFactory.getLocalPath());
		String[] albums = localDir.list();
		List<String> localAlbumList = Arrays.asList(albums);
		Picasa picasa = new Picasa(ConfigFactory.getUserName(),null);
		UserFeed uf = picasa.getGallery();
		List<AlbumEntry> remoteAlbumList = uf.getAlbumEntries();
		for (AlbumEntry album : remoteAlbumList){
			String name = album.getName();
			if (!localAlbumList.contains(name)){
				unCachedAlbumNames.add(name);
			}
		}
		
		return unCachedAlbumNames;
	}
	
	/**
	 * download an album to local_path
	 * 
	 * @param galleryName
	 * @throws IOException
	 */
	public static void downloadAlbum(String albumName) throws IOException{
//		if (userName == null || ("").equals(userName)){
//			System.out.println("userName is null,check the config.properties.");
//			return;
//		}
		if (albumName == null || ("").equals(albumName)){
			System.out.println("albumName is null.");
		}
		Picasa picasa = new Picasa(ConfigFactory.getUserName(),null);
		AlbumFeed album = picasa.getAlbum(albumName);
		String localAlbumDir = CACHE_DIR + "/" + album.getName();
		
		// save s212-c folder
//		String mediaThumbDir = localAlbumDir + "/" + SMALL_DIR_NAME;
//		String mediaThumbUrl = album.getMediaThumbnails().get(0).getUrl();
//		save(mediaThumbDir,mediaThumbUrl);
		UserFeed gallery = picasa.getGallery();
	    List<AlbumEntry> albums = gallery.getAlbumEntries();
	    for (AlbumEntry aEntry : albums){
	    	String aName = aEntry.getName();
	    	if (albumName.equals(aName)){
	    		String mediaThumbDir = localAlbumDir + "/" + SMALL_DIR_NAME;
	    		String mediaThumbUrl = aEntry.getMediaThumbnails().get(0).getUrl();
	    		save(mediaThumbDir,mediaThumbUrl);
	    	}
	    }
		
		String thumbnailDir = localAlbumDir + "/" + THUMBNAIL_DIR_NAME;
		String contentDir = localAlbumDir + "/" + CONTENT_DIR_NAME;

		List<PhotoEntry> photos = album.getPhotoEntries();
		for (PhotoEntry photo : photos){
    		// save thumbnail photos
    		save(thumbnailDir,photo.getMediaThumbnails().get(0).getUrl());
    		// save content photos
    		save(contentDir,photo.getMediaContents().get(0).getUrl());
    	}
	}
	
	/**
	 * download all albums with the userid
	 * 
	 * @throws IOException
	 */
	public static void downloadAll() throws IOException{
//		if (userName == null || ("").equals(userName)){
//			System.out.println("username is null,check config.properties");
//			return;
//		}
		Picasa picasa = new Picasa(ConfigFactory.getUserName(),null);
        UserFeed gallery = picasa.getGallery();
        List<AlbumEntry> albums = gallery.getAlbumEntries();
        for (AlbumEntry album : albums){
        	// save s212-c photos
        	String dir = CACHE_DIR + "/" + album.getName() ;
        	String remotePath = album.getMediaThumbnails().get(0).getUrl();
        	save(dir + "/" + SMALL_DIR_NAME,remotePath);
        	
        	AlbumFeed af = picasa.getAlbum(album.getName());
        	List<PhotoEntry> photos = af.getPhotoEntries();
        	String thumbnailDir = dir +"/"+ THUMBNAIL_DIR_NAME;
        	String contentDir = dir +"/"+ CONTENT_DIR_NAME;
        	for (PhotoEntry photo : photos){
        		// thunmbnail save
        		save(thumbnailDir,photo.getMediaThumbnails().get(0).getUrl());
        		// content save
        		save(contentDir,photo.getMediaContents().get(0).getUrl());
        	}
        }
	}
	
	/**
	 * download resource from remoteUrl to localDir, get the filename from the remoteUrl
	 * 
	 * @param localDir
	 * @param remoteUrl
	 * @throws IOException
	 */
	public static void save(String localDir,String remoteUrl) throws IOException{
		if (remoteUrl == null || ("").equals(remoteUrl)){
			return;
		}
		String[] strs = remoteUrl.split("/");
		String fileName = strs[strs.length-1];
		if (fileName.indexOf(".")<=0){
			return;
		}
		
		InputStream input = null;
		FileOutputStream output = null;
		try{
			URL url = new URL(remoteUrl);
			if (remoteUrl.toLowerCase().startsWith("https")){
				HttpsURLConnection httpsConn = (HttpsURLConnection)url.openConnection();
				httpsConn.setConnectTimeout(Util.CONNECT_TIMEOUT);
				httpsConn.setReadTimeout(Util.READ_TIMEOUT);
				input = httpsConn.getInputStream();
				
			}else if (remoteUrl.toLowerCase().startsWith("http")){
				URLConnection httpConn = url.openConnection();
				httpConn.setConnectTimeout(Util.CONNECT_TIMEOUT);
				httpConn.setReadTimeout(Util.READ_TIMEOUT);
				input = httpConn.getInputStream();
			
			}else{
				return;
			}
			File dir = new File(localDir);
			if (!dir.exists()){
				dir.mkdirs();
			}
			String localPath = localDir + "/" + fileName;
			File localFile = new File(localPath);
			//if there exist the file with the same name,return
			if (localFile.exists()){
				return;
			}
			output = new FileOutputStream(localFile);
			BufferedInputStream binput = new BufferedInputStream(input);
			
			int i=0;
			while ((i=binput.read())!=-1){ 
	             output.write(i);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if (input!=null){
				input.close();
				input = null;
			}
			if (output!=null){
				output.close();
				output = null;
			}
		}
	}

}
