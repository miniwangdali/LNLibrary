package com.victor.update;

import android.content.Context;
import android.widget.Toast;

public class UpdateUtil {

	private String versionName;
	private Context mContext;
	
	public UpdateUtil(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	public boolean checkUpdate(){
		if(versionName != getNewVersion()){
			
			return true;
		}else{
			return false;
		}
	}

	public String getNewVersion(){
		String newVersionName = null;
		
		return newVersionName;
	}
	
	public boolean update(){
		try{
			
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(mContext, "更新失败：" + e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
}
