package com.victor.lnlibrary.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class StaticConfig{
	
	private static boolean hasInternet = false;
	private static boolean hasSDCard = false;

  

  	public static boolean hasInternet(Context context){
  		try{
  			ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
  			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
  			if(mNetworkInfo != null && mNetworkInfo.isConnected()){
  				hasInternet = true;
  			}
  		}catch (Exception e){
  			hasInternet = false;
  		}
  		return hasInternet;
  	}

  	public static boolean hasSDCard(){
  		try{
  			hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  		}catch (Exception e){
  			hasSDCard = false;
  		}
  		return hasSDCard;
  	}

  	public static void setHasInternet(boolean hasinternet){
  		hasInternet = hasinternet;
  	}

  	public static void setHasSDCard(boolean hassdcard){
  		hasSDCard = hassdcard;
  	}
}