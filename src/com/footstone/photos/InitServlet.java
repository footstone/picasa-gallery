package com.footstone.photos;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.azib.photos.Picasa;

import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;

public class InitServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(InitServlet.class.getName());
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public InitServlet() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
    	PhotoURLCache.load();
    }
    
//    private static void cache(){
//    	long start_time = System.currentTimeMillis();
//		Picasa picasa = new Picasa(ConfigFactory.getUserName(),null);
//		// test
//        UserFeed gallery = picasa.getGallery();
//        List<AlbumEntry> albums = gallery.getAlbumEntries();
//        for (AlbumEntry album : albums){
//        	String albumName = album.getName();
//        	String remotePath = album.getMediaThumbnails().get(0).getUrl();
//        	try {
//        		String mediaKey = Util.getKey(albumName, remotePath);
//            	byte[] mediaBytes = Util.getBytesFromUrl(remotePath);
//            	// 保存相册缩略图
//            	ConfigFactory.getCache().save(mediaKey, mediaBytes);
//		    	// 保存相册中的图
//		    	AlbumFeed af = picasa.getAlbum(album.getName());
//		    	
//		    	List<PhotoEntry> photos = af.getPhotoEntries();
//		    	for (PhotoEntry photo : photos){
//		    		String thumbnailUrl = photo.getMediaThumbnails().get(0).getUrl();
//		    		String thumbnailKey = Util.getKey(albumName, thumbnailUrl);
//		    		byte[] thumbnailData = Util.getBytesFromUrl(thumbnailUrl);
//		    		// 保存小图
//		    		ConfigFactory.getCache().save(thumbnailKey, thumbnailData);
//		    		
//		    		String contentUrl = photo.getMediaContents().get(0).getUrl();
//		    		String contentKey = Util.getKey(albumName, contentUrl);
//		    		byte[] contentData = Util.getBytesFromUrl(contentUrl);
//		    		// 保存大图
//		    		ConfigFactory.getCache().save(contentKey, contentData);
//		    	}
//        	} catch (Exception e) {
//				log.severe("缓存图片数据失败!"+e.getMessage());
//				e.printStackTrace();
//			}
//        }
//        long end_time = System.currentTimeMillis();
//        log.severe("缓存完成，耗时:"+(end_time-start_time)/1000+"s");
//    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	
}
