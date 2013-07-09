package com.footstone.photos.proc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import net.azib.photos.Picasa;

import com.footstone.photos.ConfigFactory;
import com.footstone.photos.Util;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.UserFeed;

public class Pack {
	
	private static String CACHE_DIR = ConfigFactory.getLocalPath();
	public static final String THUMBNAIL_DIR_NAME = "s144-c";
	public static final String CONTENT_DIR_NAME = "s1600";
	public static final String SMALL_DIR_NAME = "s212-c";
	
	public static void main(String[] args) throws IOException {
		long start_time = System.currentTimeMillis();

		Picasa picasa = new Picasa("njzeroc",null);
        UserFeed gallery = picasa.getGallery();
        List<AlbumEntry> albums = gallery.getAlbumEntries();
        for (AlbumEntry album : albums){
        	// save s212-c photos
        	String dir = CACHE_DIR + album.getName() + "/" +SMALL_DIR_NAME;
        	String remotePath = album.getMediaThumbnails().get(0).getUrl();
        	save(dir,remotePath);
        }
		long end_time = System.currentTimeMillis();
	
		System.out.println("all download cost:" + (end_time - start_time)/1000 + "s");
	
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
			// ����ļ�������ȡ������
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
