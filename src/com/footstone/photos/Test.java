package com.footstone.photos;

import java.io.File;
import java.util.List;

import net.azib.photos.Picasa;

import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;

public class Test {
	
	public static void main(String[] args) {
	    File f = new File("C:/Users/Kevin/Desktop/pack/");
	    File[] files = f.listFiles();
	    for (int i=0,len=files.length;i<len;i++){
	    	System.out.println(files[i].getAbsolutePath());
	    }
	}
	
	public static void main2(String[] args) {
		Picasa picasa = new Picasa("njzeroc",null);
		
		// test
        UserFeed gallery = picasa.getGallery();
        
        System.out.println("===================================");
        System.out.println("urlSuffix:"+picasa.getUrlSuffix());
        System.out.println("===================================");
        System.out.println("gallery id: "+gallery.getId());
        System.out.println("ExtensionLocalName: "+gallery.getExtensionLocalName());
        System.out.println("gallery logo: "+gallery.getLogo());
        System.out.println("plain text: "+gallery.getTitle().getPlainText());
        System.out.println("username: "+gallery.getUsername());
        System.out.println("nickname: "+gallery.getNickname());
        System.out.println("===================================");
        System.out.println("===================================");
        System.out.println("===================================");
        List<AlbumEntry> albums = gallery.getAlbumEntries();
        for (AlbumEntry entry : albums){
        	System.out.println("id:"+entry.getId());
        	System.out.println(entry.getPhotosUsed());
        	System.out.println("location:"+entry.getLocation());
        	System.out.println("kind:"+entry.getKind());
        	System.out.println("name:"+entry.getName());
        	System.out.println("nickname:"+entry.getNickname());
        	//System.out.println("plainTextContext:"+entry.getPlainTextContent());
        	System.out.println("SelectedFields:"+entry.getSelectedFields());
        	System.out.println("userName:"+entry.getUsername());
        	System.out.println("location:"+entry.getCategories());
        	System.out.println("plainText:"+entry.getTitle().getPlainText());
        	System.out.println("href:"+entry.getHtmlLink().getHref());
        	System.out.println("url:"+entry.getMediaThumbnails().get(0).getUrl());
        	System.out.println("htmlLink:"+entry.getHtmlLink().getHref());
        	System.out.println("key:"+Util.getKey(entry.getName(), entry.getMediaThumbnails().get(0).getUrl()));
        	System.out.println("--------------------------------------");
        	//mediaThumbnails[0].url
        	AlbumFeed af = picasa.getAlbum(entry.getName());
        	List<PhotoEntry> photos = af.getPhotoEntries();
        	for (PhotoEntry photo:photos){
        		System.out.println("photo thumbnails:"+photo.getMediaThumbnails().get(0).getUrl()+"photo url:"+photo.getMediaContents().get(0).getUrl()+" ");
        	}
        	
        	System.out.println("===================================");
        }
	}

}
