package com.footstone.photos;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import net.azib.photos.Picasa;

import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;

public class PhotoCache {
	
	private static String tt = "https://lh3.googleusercontent.com/-gMPFNSgj_ac/Ub1RXcHxK0E/AAAAAAAACik/UgV921b2aNk/s212-c/2012926.jpg";
	private static String t2 = "http://pic.dhe.ibm.com/infocenter/dmanager/v7r5/topic/com.ibm.dserver.overview/Content/Business_Rules/Decision_Server_dist/Overview/_media/diag_architecture_default.jpg";
	private static String t3 = "https://www.alipay.com/";
	private static String t5 = "http://lh6.ggpht.com/-1gWOj4ps0zg/Ub1fLud6o7I/AAAAAAAAJkU/BaMFDUoFqyI/s144-c/_MG_1059_S750ShI.jpg";
	private static String t6 = "https://lh6.googleusercontent.com/-J5UfjX7P9bo/Ub1FB0IadrE/AAAAAAAACi8/om5MdSAoDg0/s212-c/201303.jpg";
	
	private static String localPath = "D:/pic-cache/tt.jpg";
	
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
        	// 保存相册缩略图
        	String dir = CACHE_DIR + album.getName() + "/" +SMALL_DIR_NAME;
        	String remotePath = album.getMediaThumbnails().get(0).getUrl();
        	save(dir,remotePath);
        }
		long end_time = System.currentTimeMillis();
	
		System.out.println("all download cost:" + (end_time - start_time)/1000 + "s");
	
	}
	
	public static void pack01(){
		File dir = new File(CACHE_DIR);
		File[] files = dir.listFiles();
		for (int i=0,len=files.length;i<len;i++){
			File file = files[i];
			File f = new File(file.getPath()+"/"+SMALL_DIR_NAME);
			if (f.exists()){
				f.mkdir();
			}
		}
	}
	
	public static void checkFileSize(String path) throws IOException{
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (int i=0,len=files.length;i<len;i++){
			File file = files[i];
			if (file.isDirectory()){
				checkFileSize(file.getPath());
			}else{
				long size = file.length();
				if (size > 1024 * 1024 ){
					System.out.println(file.getPath()+" "+file.getName());
				}
			}
			
		}
	}
	
	
	
	/**
	 * TODO
	 * TODO
	 * TODO
	 * 
	 * @param localDir
	 * @param remoteUrl
	 * @throws IOException
	 */
	public static void byteSave(String localDir,String remoteUrl) throws IOException{
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
			
			//List<byte> list = new ArrayList<byte>();
			
			File dir = new File(localDir);
			if (!dir.exists()){
				dir.mkdirs();
			}
			
			String localPath = localDir + "/" + fileName;
			File localFile = new File(localPath);
			// 如果文件存在则取消下载
			if (localFile.exists()){
				return;
			}
			output = new FileOutputStream(localFile);
			byte[] data = Util.input2Bytes(input);
			output.write(data);
//			BufferedInputStream binput = new BufferedInputStream(input);
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//			byte[] temp = new byte[1024];
//			int len=0;
//			while ((len=binput.read(temp))!=-1){ 
//	            bos.write(temp, 0, len);
//			}
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
	
	/**
	 * 
	 * @throws IOException
	 */
	public static void cache() throws IOException{
		Picasa picasa = new Picasa("njzeroc",null);
		// test
        UserFeed gallery = picasa.getGallery();
        List<AlbumEntry> albums = gallery.getAlbumEntries();
        for (AlbumEntry album : albums){
        	// 保存相册缩略图
        	String dir = CACHE_DIR + album.getName();
        	String remotePath = album.getMediaThumbnails().get(0).getUrl();
        	save(dir,remotePath);
        	// 保存相册中的图
        	AlbumFeed af = picasa.getAlbum(album.getName());
        	List<PhotoEntry> photos = af.getPhotoEntries();
        	String thumbnailDir = dir +"/"+ THUMBNAIL_DIR_NAME;
        	String contentDir = dir +"/"+ CONTENT_DIR_NAME;
        	for (PhotoEntry photo : photos){
        		// 保存小图
        		save(thumbnailDir,photo.getMediaThumbnails().get(0).getUrl());
        		// 保存大图
        		save(contentDir,photo.getMediaContents().get(0).getUrl());
        	}
        }
	}
	
	private static void delete(File dir) throws IOException{
		
	}
	
	/**
	 * 清空指定文件夹下所有内容
	 * TODO 映射成一颗树实现
	 * @param dirName
	 * @throws IOException
	 */
	public static void clear(String dirName) throws IOException{
		if (dirName == null || ("").equals(dirName)){
			return;
		}
		File dir = new File(dirName);
		File[] files = dir.listFiles();
		if (files!=null && files.length>0){
			for (int i=0,len=files.length;i<len;i++){
				File file = files[i];
				if (file.isFile()){
					file.delete();
				}else if (file.isDirectory()){
					if (!dirName.endsWith("/")){
						clear(dirName+file.getName());
					}else{
						clear(dirName+"/"+file.getName());
					}
					
				}
			}
		// 删除空文件夹
		}else{
			dir.delete();
		}
	}
	
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
			// 如果文件存在则取消下载
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

	public static void cached(String addr) throws Exception{
		URL url = new URL(addr); 
		
		//HttpsURLConnection uc = (HttpsURLConnection)url.openConnection(); 
		URLConnection uc = url.openConnection();
		
//		print_https_cert(uc);
//		print_content(uc);
		
        InputStream is = uc.getInputStream(); 
        File file =  new File(localPath); 
        java.io.FileOutputStream out = new FileOutputStream(file); 
        int i=0; 
        while ((i=is.read())!=-1){ 
                out.write(i); 
        } 
        is.close();
	}
	
	private static void print_https_cert(HttpsURLConnection con){
		 
	    if(con!=null){
	 
	      try {
	 
		System.out.println("Response Code : " + con.getResponseCode());
		System.out.println("Cipher Suite : " + con.getCipherSuite());
		System.out.println("\n");
	 
		Certificate[] certs = con.getServerCertificates();
		for(Certificate cert : certs){
		   System.out.println("Cert Type : " + cert.getType());
		   System.out.println("Cert Hash Code : " + cert.hashCode());
		   System.out.println("Cert Public Key Algorithm : " 
	                                    + cert.getPublicKey().getAlgorithm());
		   System.out.println("Cert Public Key Format : " 
	                                    + cert.getPublicKey().getFormat());
		   System.out.println("\n");
		}
	 
		} catch (SSLPeerUnverifiedException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	 
	     }
	 
	   }
	 
	   private static void print_content(HttpsURLConnection con){
		if(con!=null){
	 
		try {
	 
		   System.out.println("****** Content of the URL ********");			
		   BufferedReader br = 
			new BufferedReader(
				new InputStreamReader(con.getInputStream()));
	 
		   String input;
	 
		   while ((input = br.readLine()) != null){
		      System.out.println(input);
		   }
		   br.close();
	 
		} catch (IOException e) {
		   e.printStackTrace();
		}
	 
	       }
	 
	   }
	 
	
	public static void main2(String[] args) throws Exception{
		cached(t3);
		//AuthSubUtil.get
//		PicasawebService myService = new PicasawebService("njzeroc");
//		//myService.set
//		//myService.setUserCredentials("njzeroc@gmail.com", "Silence10");
//		
//		URL feedUrl = new URL("https://picasaweb.google.com/data/feed/api/user/njzeroc?kind=album");
//
//		UserFeed myUserFeed = myService.getFeed(feedUrl, UserFeed.class);
//
//		for (AlbumEntry myAlbum : myUserFeed.getAlbumEntries()) {
//		    System.out.println(myAlbum.getTitle().getPlainText()+" "+myAlbum.getSelfLink().getHref());
//		}
		
		
		
		
	}

}
