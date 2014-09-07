package com.victor.lnlibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.bean.FastBlur;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.config.StaticConfig;
import com.victor.lnlibrary.dao.LoadBookTask;

public class LaunchActivity extends Activity{
	
	private RelativeLayout backLayout;
	private LinearLayout blurLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		Library.getMyLibrary().clear();
		
		blurLayout = (LinearLayout)findViewById(R.id.blurlayout);
		backLayout = (RelativeLayout)findViewById(R.id.background);
		backLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				BitmapDrawable bd = (BitmapDrawable)backLayout.getBackground();
				blur(bd.getBitmap(), blurLayout);
			}
		});
		
		
		ProgressBar progressBar = (ProgressBar)findViewById(R.id.loadingprogress);
		
		if(StaticConfig.hasSDCard()){
			new LoadBookTask(this, progressBar).execute("");
		}else{
			Toast.makeText(this, "无sd卡", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void blur(Bitmap bkg, View view) {
		//毛玻璃还有点问题……
		/*float scaleFactor = 8 ;
	    float radius = 3;

	    Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
	            (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(overlay);
	    canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
	    canvas.scale(1 / scaleFactor, 1 / scaleFactor);
	    Paint paint = new Paint();
	    paint.setFlags(Paint.FILTER_BITMAP_FLAG);
	    canvas.drawBitmap(bkg, 0, 0, paint);
	 
	    overlay = FastBlur.doBlur(overlay, (int)radius, true);
	    view.setBackgroundDrawable(new BitmapDrawable(getResources(), overlay));*/
	}
}