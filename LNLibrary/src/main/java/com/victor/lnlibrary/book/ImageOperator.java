package com.victor.lnlibrary.book;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class ImageOperator{
	private static String FILEPATH = "/LNLibrary/";
	private String SDPATH = Environment.getExternalStorageDirectory().getPath();

	public Bitmap downloadImage(String imagelink){
		try{
			URL url = new URL(imagelink);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			Bitmap image = BitmapFactory.decodeStream(conn.getInputStream());
			
			return image;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Bitmap loadImage(String imagename){
		try{
			Bitmap image = BitmapFactory.decodeStream(new FileInputStream(SDPATH + FILEPATH + "Images/" + imagename + ".ill"));
			return image;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public boolean saveImage(Bitmap image, String bookname, String imagename){
		FileOperator operator = new FileOperator();
		try{
			operator.writeFile("Images/" + bookname, imagename + ".ill", image);
			return true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
}