package com.victor.lnlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.config.StaticConfig;
import com.victor.lnlibrary.dao.LoadBookTask;

public class LaunchActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		if(StaticConfig.hasSDCard()){
			new LoadBookTask(this).execute("");
		}else{
			
		}
	}
}