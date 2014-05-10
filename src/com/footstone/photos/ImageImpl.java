package com.footstone.photos;

public class ImageImpl implements IImage{
	private static long COUNT = 0L;
	private static Object LOCK = new Object();
	
	public String getImageUrl() {
		int imageSize = ConfigFactory.getImageList().size();
		if (imageSize == 0){
			return "";
		}
		int index =(int) COUNT%imageSize;
		String imageUrl = ConfigFactory.getImageList().get(index);
		synchronized(LOCK){
			COUNT++;
		}
		return imageUrl;
	}

	public static void main(String args[]){
//		long c = 10022204;
//		int a = 3;
//		int b = (int) c%a;
//		System.out.println(b);
		
		int count = 20;
		for (int i=0;i<count;i++){
			IImage image = new ImageImpl();
			System.out.println(image.getImageUrl());
			//System.out.println(COUNT+" "+image.getImageUrl());
		}
	}
}
