package com.victor.lnlibrary.dao;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;
import com.victor.lnlibrary.book.ImageOperator;

public class ImageLoadTask extends AsyncTask<String, Integer, String>{
	private String bookname;
	private String imageLink;
	private String imagePath = null;
	private ImageView imageView;
	private String imagename;
	private Activity mActivity;
	
	public ImageLoadTask(Activity activity, ImageView imageView, String imagepath, String bookname, String imagename){
    	mActivity = activity;
    	this.imageView = imageView;
    	imageLink = imagepath;
    	this.bookname = bookname;
    	this.imagename = imagename;
	}
	
	
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try{
			ImageOperator operator = new ImageOperator();
		    if (operator.saveImage(operator.downloadImage(imageLink), bookname + "/", imagename)){
		    	imagePath = new String();
		        imagePath = (bookname + "/" + imagename);
		        return "success";
		    }
		    return null;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return e.toString();
		}
	}
	
	
	
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(result.equals("success")){
			try{
				ImageOperator operator = new ImageOperator();
				Bitmap image = operator.loadImage(imagePath);
			    imageView.setImageBitmap(image);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Toast.makeText(mActivity, "图片加载失败", Toast.LENGTH_SHORT).show();
			}
		}else {
			Toast.makeText(mActivity, "图片下载失败", Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}



	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}



	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	

}