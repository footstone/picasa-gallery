package com.footstone.photos;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Util {
	
	public static final int CONNECT_TIMEOUT = 10*1000;
	public static final int READ_TIMEOUT = 10*1000;
	public static final String KEY_SPLIT = "-";

	/**
	 * @param albumName
	 * @param remoteUrl
	 * @return
	 * @throws Exception
	 */
	public static String getKey(String albumName,String remoteUrl) {
		if (remoteUrl == null || ("").equals(remoteUrl)){
			return null;
		}
		String[] strs = remoteUrl.split("/");
		String fileName = strs[strs.length-1];
		String dir = strs[strs.length-2];
		StringBuffer sb = new StringBuffer();
		sb.append(albumName).append(KEY_SPLIT).append(dir).append(KEY_SPLIT).append(fileName);
		return sb.toString();
	}
	
	/**
	 * 
	 * @param albumName
	 * @param remoteUrl
	 * @return
	 */
	public static String getImageUrl(String albumName,String remoteUrl){
		if (remoteUrl == null || ("").equals(remoteUrl)){
			return null;
		}
		IImage image = new ImageImpl();
		String imageUrl= image.getImageUrl();
		if (imageUrl==""){
			return "";
		}
		
		String[] strs = remoteUrl.split("/");
		String fileName = strs[strs.length-1];
		String dir = strs[strs.length-2];
		StringBuilder sb = new StringBuilder();
		sb.append(imageUrl).append(albumName).append("/").append(dir).append("/").append(fileName);
		
		return sb.toString();
	}
	
	/**
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] input2Bytes(InputStream input) throws IOException{
		if (input ==  null){
			return null;
		}
		byte[] temp = new byte[1024];
		BufferedInputStream bin = new BufferedInputStream(input);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = 0;
		while((len=bin.read(temp))!=-1){
			bos.write(temp, 0, len);
		}
		return bos.toByteArray();
	}
	
	/**
	 * 
	 * @param remoteUrl
	 * @return
	 * @throws IOException
	 */
	public static String getFileNameFromUrl(String remoteUrl){
		if (remoteUrl == null || ("").equals(remoteUrl)){
			return null;
		}
		String[] strs = remoteUrl.split("/");
		String fileName = strs[strs.length-1];
		if (fileName.indexOf(".")<=0){
			return null;
		}
		return fileName;
	}
	
	/**
	 * 
	 * @param remoteUrl
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromUrl(String remoteUrl) throws IOException{
		if (remoteUrl == null || ("").equals(remoteUrl)){
			return null;
		}
		InputStream input = null;
		byte[] data = null;
		try{
			URL url = new URL(remoteUrl);
			
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(CONNECT_TIMEOUT);
			uc.setReadTimeout(READ_TIMEOUT);
			input = uc.getInputStream();
			
//			if (remoteUrl.toLowerCase().startsWith("https")){
//				HttpsURLConnection httpsConn = (HttpsURLConnection)url.openConnection();
//				httpsConn.setConnectTimeout(CONNECT_TIMEOUT);
//				httpsConn.setReadTimeout(READ_TIMEOUT);
//				input = httpsConn.getInputStream();
//				
//			}else if (remoteUrl.toLowerCase().startsWith("http")){
//				URLConnection httpConn = url.openConnection();
//				httpConn.setConnectTimeout(CONNECT_TIMEOUT);
//				httpConn.setReadTimeout(READ_TIMEOUT);
//				input = httpConn.getInputStream();
//			
//			}else{
//				return data;
//			}
			data = input2Bytes(input);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if (input!=null){
				input.close();
				input = null;
			}
		}
		return data;
	}

}
