package com.victor.lnlibrary.dao;

import com.victor.lnlibrary.htmlparser.Update;
import com.victor.update.UpdateUtil;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.widget.Toast;

public class UpdateTask extends AsyncTask<String, Integer, String> {
	
	private Activity mActivity;
	private String newVersionName;
	private String newVersionInfo;
	private String oldVersionName;
	

	public UpdateTask(Activity activity) {
		super();
		// TODO Auto-generated constructor stub
		mActivity = activity;
		
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(result.equals("update")){
			Toast.makeText(mActivity, "有新版本!", Toast.LENGTH_SHORT).show();
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
