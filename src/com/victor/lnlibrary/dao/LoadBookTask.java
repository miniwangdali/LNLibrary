package com.victor.lnlibrary.dao;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.victor.lnlibrary.MainActivity;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.book.BookParser;
import com.victor.lnlibrary.book.FileOperator;
import java.io.File;
import java.io.FileInputStream;

public class LoadBookTask extends AsyncTask<String, Integer, String>{
	private Activity mActivity;
	
	public LoadBookTask(Activity activity){
    	mActivity = activity;
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		FileOperator operator = new FileOperator();
		if(operator.isFileExist("Books")){
			File[] files = operator.readFiles("Books");
			for(int i = 0; i < files.length; i ++){
				try{
					FileInputStream fis = new FileInputStream(files[i]);
			        Library.addBook(new BookParser().parse(fis));
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return e.toString();
				}
			}
			return "success";
		}else{
			return "empty";
		}
	}
	
	
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(result.equals("success")){
			Intent intent = new Intent();
		    intent.setClass(mActivity, MainActivity.class);
		    mActivity.startActivity(intent);
		    mActivity.finish();
		    Toast.makeText(mActivity, "本地书目加载成功", Toast.LENGTH_SHORT).show();
		}else if(result.equals("empty")){
			Intent intent = new Intent();
		    intent.setClass(mActivity, MainActivity.class);
		    mActivity.startActivity(intent);
		    mActivity.finish();
			Toast.makeText(mActivity, "无书目", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
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