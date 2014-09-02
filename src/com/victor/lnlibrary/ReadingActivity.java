package com.victor.lnlibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.bean.Config;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.book.ChapterContent;
import com.victor.lnlibrary.book.ImageOperator;
import com.victor.lnlibrary.dao.ImageLoadTask;
import com.victor.lnlibrary.htmlparser.Content;
import com.victor.lnlibrary.ui.MyTextView;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ReadingActivity extends Activity{
	private String bookname;
	private int chapterId;
	private String chaptertitle;
	private ChapterContent contents;
	private String dossiername;
	private int fullHeight;
	private int pageHeight;
	private LinearLayout readingContent;
	private ScrollView scrollView;
	Activity self = this;
	private double progress;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reading);
		
		Intent intent = getIntent();
		bookname = intent.getStringExtra("bookname");
	    dossiername = intent.getStringExtra("dossiername");
	    chaptertitle = intent.getStringExtra("chapter");
	    chapterId = Library.getTempBook().getDossier(dossiername).getChapterId(chaptertitle);
	    
	    TextView chapterTitleText = (TextView)findViewById(R.id.chaptertitle);
	    chapterTitleText.setText(chaptertitle);
	    
	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(Intent.ACTION_TIME_TICK);
	    registerReceiver(timeReceiver, intentFilter);

	    
	    scrollView = (ScrollView)findViewById(R.id.readingscroll);
	    ViewTreeObserver vto = scrollView.getViewTreeObserver();
	    vto.addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				pageHeight = scrollView.getHeight();
				fullHeight = scrollView.getMeasuredHeight();
				return true;
			}
		});
	    
	    readingContent = (LinearLayout)findViewById(R.id.contentlayout);
	    contents = Library.getTempBook().getDossier(dossiername).getChapterContent(chaptertitle);
	    List<String> contentList = contents.getContents();
		List<String> imageList = contents.getImageList();
		for(int i = 0; i < contentList.size(); i ++){
			MyTextView contentTextView = new MyTextView(this);
			contentTextView.setText(contentList.get(i));
			contentTextView.setTextSize(Config.getFontsize());
			contentTextView.setLineSpacing(3.0f, Config.getLinespace());
			readingContent.addView(contentTextView);
			if(i < imageList.size()){
				if(Library.getTempBook().getDossier(dossiername).isDownloaded()){
					ImageOperator operator = new ImageOperator();
					Bitmap image = operator.loadImage(imageList.get(i));
					ImageView imageView = new ImageView(this);
					imageView.setImageBitmap(image);
					imageView.setScaleType(ScaleType.FIT_CENTER);
					readingContent.addView(imageView);
				}else{
					ImageView imageView = new ImageView(this);
					new ImageLoadTask(this, imageView, imageList.get(i), bookname, "tempImage" + i).execute("");
					imageView.setScaleType(ScaleType.FIT_CENTER);
					readingContent.addView(imageView);
				}
			}
			scrollView.scrollTo(0, 0);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.reading, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if(readingContent.getMeasuredHeight() <= scrollView.getScrollY() + pageHeight){
				if(chapterId == Library.getTempBook().getDossier(dossiername).getChapterContents().size() - 1){
					Toast.makeText(this, "已经是本卷末了", Toast.LENGTH_SHORT).show();
				}else{
					loadNextChapter();
					TextView progressText = (TextView)findViewById(R.id.progress);
					progressText.setText("当前进度：0.00%");
				}
			}else{
				scrollView.scrollTo(0, scrollView.getScrollY() + pageHeight);
				progress = 100.0 * (scrollView.getScrollY() + pageHeight) / readingContent.getMeasuredHeight();
				BigDecimal bigDecimal = new BigDecimal(progress);
				progress = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				TextView progressText = (TextView)findViewById(R.id.progress);
				progressText.setText("当前进度：" + String.valueOf(progress) + "%");
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			if(scrollView.getScrollY() == 0){
				if(chapterId == 0){
					Toast.makeText(this, "已经是本卷初了", Toast.LENGTH_SHORT).show();
				}else{
					loadPreviousChapter();
					TextView progressText = (TextView)findViewById(R.id.progress);
					progressText.setText("当前进度：0.00%");
				}
			}else{
				scrollView.scrollTo(0, scrollView.getScrollY() - pageHeight);
				progress = 100.0 * (scrollView.getScrollY() + pageHeight) / readingContent.getMeasuredHeight();
				BigDecimal bigDecimal = new BigDecimal(progress);
				progress = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				TextView progressText = (TextView)findViewById(R.id.progress);
				progressText.setText("当前进度：" + String.valueOf(progress) + "%");
			}
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;

		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	private BroadcastReceiver timeReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){

			if (intent.getAction().equals(Intent.ACTION_TIME_TICK)){
				String hour = new String();
				String minute = new String();
				Time time = new Time();
				time.setToNow();
				if(time.hour >= 10){
					hour = String.valueOf(time.hour);
				}else{
					hour = "0" + String.valueOf(time.hour);
				}
				if(time.minute >= 10){
					minute = String.valueOf(time.minute);
				}else{
					minute = "0" + String.valueOf(time.minute);
				}
				TextView timeText = (TextView)findViewById(R.id.time);
				timeText.setText(hour + ":" + minute);
			}
		}};
	
	private void loadNextChapter(){
    	readingContent.removeAllViews();
    	chapterId = chapterId + 1;
    	chaptertitle = ((ChapterContent)Library.getTempBook().getDossier(dossiername).getChapterContents().get(chapterId)).getChaptertitle();
    	contents = ((ChapterContent)Library.getTempBook().getDossier(dossiername).getChapterContents().get(chapterId));
    	if(contents.getContents().size() == 0){
    		new TempTask().execute("");
    	}else{
    		TextView chapterTitleText = (TextView)findViewById(R.id.chaptertitle);
    		chapterTitleText.setText(chaptertitle);
    		List<String> contentList = contents.getContents();
    		List<String> imageList = contents.getImageList();
    		for(int i = 0; i < contentList.size(); i ++){
    			MyTextView contentTextView = new MyTextView(this);
    			contentTextView.setText(contentList.get(i));
    			contentTextView.setTextSize(Config.getFontsize());
    			contentTextView.setLineSpacing(3.0f, Config.getLinespace());
    			readingContent.addView(contentTextView);
    			if(i < imageList.size()){
    				if(Library.getTempBook().getDossier(dossiername).isDownloaded()){
    					ImageOperator operator = new ImageOperator();
    					Bitmap image = operator.loadImage(imageList.get(i));
    					ImageView imageView = new ImageView(this);
    					imageView.setImageBitmap(image);
    					imageView.setScaleType(ScaleType.CENTER_INSIDE);
    					readingContent.addView(imageView);
    				}else{
    					ImageView imageView = new ImageView(this);
    					new ImageLoadTask(this, imageView, imageList.get(i), bookname, "tempImage" + i).execute("");
    					imageView.setScaleType(ScaleType.CENTER_INSIDE);
    					readingContent.addView(imageView);
    				}
    			}
    			scrollView.scrollTo(0, 0);
    		}
    	}
    }

	private void loadPreviousChapter(){
		readingContent.removeAllViews();
    	chapterId = chapterId - 1;
    	chaptertitle = ((ChapterContent)Library.getTempBook().getDossier(dossiername).getChapterContents().get(chapterId)).getChaptertitle();
    	contents = ((ChapterContent)Library.getTempBook().getDossier(dossiername).getChapterContents().get(chapterId));
    	if(contents.getContents().size() == 0){
    		new TempTask().execute("");
    	}else{
    		TextView chapterTitleText = (TextView)findViewById(R.id.chaptertitle);
    		chapterTitleText.setText(chaptertitle);
    		List<String> contentList = contents.getContents();
    		List<String> imageList = contents.getImageList();
    		for(int i = 0; i < contentList.size(); i ++){
    			MyTextView contentTextView = new MyTextView(this);
    			contentTextView.setText(contentList.get(i));
    			contentTextView.setTextSize(Config.getFontsize());
    			contentTextView.setLineSpacing(3.0f, Config.getLinespace());
    			readingContent.addView(contentTextView);
    			if(i < imageList.size()){
    				if(Library.getTempBook().getDossier(dossiername).isDownloaded()){
    					ImageOperator operator = new ImageOperator();
    					Bitmap image = operator.loadImage(imageList.get(i));
    					ImageView imageView = new ImageView(this);
    					imageView.setImageBitmap(image);
    					imageView.setScaleType(ScaleType.CENTER_INSIDE);
    					readingContent.addView(imageView);
    				}else{
    					ImageView imageView = new ImageView(this);
    					new ImageLoadTask(this, imageView, imageList.get(i), bookname, "tempImage" + i).execute("");
    					imageView.setScaleType(ScaleType.CENTER_INSIDE);
    					readingContent.addView(imageView);
    				}
    			}
    			scrollView.scrollTo(0, 0);
    		}
    	}
	}
	
	private class TempTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				ChapterContent chapterContent = Library.getTempBook().getDossier(dossiername).getChapterContent(chaptertitle);
		        Content content = new Content(chapterContent.getChapterlink());
		        List<String> contentList = Arrays.asList(content.getText().split("@img@"));
		        List<String> imageList = content.getLinkList();
		        chapterContent.setContents(contentList);
		        chapterContent.setImageList(imageList);
		        
		        return "success";
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
				TextView chapterTitleText = (TextView)findViewById(R.id.chaptertitle);
				chapterTitleText.setText(chaptertitle);
				List<String> contentList = contents.getContents();
	    		List<String> imageList = contents.getImageList();
	    		for(int i = 0; i < contentList.size(); i ++){
	    			MyTextView contentTextView = new MyTextView(self);
	    			contentTextView.setText(contentList.get(i));
	    			contentTextView.setTextSize(Config.getFontsize());
	    			contentTextView.setLineSpacing(3.0f, Config.getLinespace());
	    			readingContent.addView(contentTextView);
	    			if(i < imageList.size()){
	    				if(Library.getTempBook().getDossier(dossiername).isDownloaded()){
	    					ImageOperator operator = new ImageOperator();
	    					Bitmap image = operator.loadImage(imageList.get(i));
	    					ImageView imageView = new ImageView(self);
	    					imageView.setImageBitmap(image);
	    					imageView.setScaleType(ScaleType.CENTER_INSIDE);
	    					readingContent.addView(imageView);
	    				}else{
	    					ImageView imageView = new ImageView(self);
	    					new ImageLoadTask(self, imageView, imageList.get(i), bookname, "tempImage" + i).execute("");
	    					imageView.setScaleType(ScaleType.CENTER_INSIDE);
	    					readingContent.addView(imageView);
	    				}
	    			}
	    			scrollView.scrollTo(0, 0);
	    		}
				
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(timeReceiver);
		super.onDestroy();
	}
	
	
	
}