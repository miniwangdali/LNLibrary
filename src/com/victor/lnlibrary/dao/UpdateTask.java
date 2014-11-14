package com.victor.lnlibrary.dao;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.victor.lnlibrary.htmlparser.Update;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.widget.Toast;

public class UpdateTask extends AsyncTask<String, Integer, String> {
	
	private Activity mActivity;
	private String newVersionName;
	private String newVersionInfo;
	private String oldVersionName;
	private String downloadLink;

	public UpdateTask(Activity activity) {
		super();
		// TODO Auto-generated constructor stub
		mActivity = activity;
		
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(result.equals("update")){
			//Toast.makeText(mActivity, "有新版本!", Toast.LENGTH_SHORT).show();
			Builder mBuilder = new Builder(mActivity);
			mBuilder.setTitle("发现新版本！");
			mBuilder.setMessage(newVersionInfo);
			mBuilder.setPositiveButton("下载", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					apkDownTask mApkDownTask = new apkDownTask(mActivity, downloadLink);
					mApkDownTask.executeOnExecutor(Executors.newCachedThreadPool(), "");
				}
			});
			mBuilder.setNegativeButton("下次再说", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			Dialog mDialog = mBuilder.create();
			mDialog.show();
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

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try {
			Update mUpdate = new Update();
			newVersionName = mUpdate.getVersionName();
			newVersionInfo = mUpdate.getNewVersionInfo();
			downloadLink = mUpdate.getDownloadLink();
			
			oldVersionName = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionName;
			if(oldVersionName != newVersionName && newVersionName != null){
				return "update";
			}else{
				return null;
			}
		}catch(NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
