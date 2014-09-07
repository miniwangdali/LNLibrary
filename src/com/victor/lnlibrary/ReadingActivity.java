package com.victor.lnlibrary;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextPaint;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.book.BookParser;
import com.victor.lnlibrary.book.ChapterContent;
import com.victor.lnlibrary.book.FileOperator;
import com.victor.lnlibrary.book.ImageOperator;
import com.victor.lnlibrary.config.Config;
import com.victor.lnlibrary.dao.ImageLoadTask;
import com.victor.lnlibrary.htmlparser.Content;
import com.victor.lnlibrary.ui.FoldMenu;
import com.victor.lnlibrary.ui.MyTextView;

import java.math.BigDecimal;
import java.util.ArrayList;
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
	private FoldMenu foldMenu;
	
	private int cLines;
	private int cLetters;
	private int currentViewNumber = 0;
	
	private PowerManager powerManager = null;
	private WakeLock wakeLock = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reading);
		
		if(Config.isAwake()){
			powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
			wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "reading lock");
		}
		
		
		Intent intent = getIntent();
		bookname = intent.getStringExtra("bookname");
	    dossiername = intent.getStringExtra("dossiername");
	    chaptertitle = intent.getStringExtra("chapter");
	    chapterId = Library.getTempBook().getDossier(dossiername).getChapterId(chaptertitle);
	    Library.getBook(bookname).getDossier(dossiername).setLastRead(chapterId);
	    progress = Library.getTempBook().getDossier(dossiername).getChapterContent(chaptertitle).getProgress();
	    
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
				return true;
			}
		});
	    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				if(Library.getTempBook().getDossier(dossiername).getChapterContent(chaptertitle).getProgress() < 0){
					scrollView.scrollTo(0, 0);
				}else{
					Double p = Library.getTempBook().getDossier(dossiername).getChapterContent(chaptertitle).getProgress();
					scrollView.scrollTo(0, (int)(p * readingContent.getMeasuredHeight()) / 100 - pageHeight);
				}
			}
		});
	    vto.addOnScrollChangedListener(new OnScrollChangedListener() {
			
			@Override
			public void onScrollChanged() {
				// TODO Auto-generated method stub
				TextView progressText = (TextView)findViewById(R.id.progress);
				progressText.setText("当前进度：" + String.valueOf(progress) + "%");
			}
		});
	    
	    cLines = countLines();
	    cLetters = countLetters();
	    
	    readingContent = (LinearLayout)findViewById(R.id.contentlayout);
	    contents = Library.getTempBook().getDossier(dossiername).getChapterContent(chaptertitle);
	    List<String> contentList = contents.getContents();
		List<String> imageList = contents.getImageList();
		for(int i = 0; i < contentList.size(); i ++){
			
			List<String> dividedText = contentDivider(contentList.get(i), cLines, cLetters);
			for(String text : dividedText){
				MyTextView contentTextView = new MyTextView(this);
				contentTextView.setTextSize(Config.getFontsize());
				contentTextView.setLineSpacing(3.0f, Config.getLinespace());
				contentTextView.setText(text);
				readingContent.addView(contentTextView);
			}
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
			
		}
		
		
		
		//FoldMenu
		foldMenu = (FoldMenu)findViewById(R.id.id_foldmenu);
		ImageView nextImageView = (ImageView)foldMenu.findViewWithTag("Next");
		nextImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadNextChapter();
				TextView progressText = (TextView)findViewById(R.id.progress);
				progressText.setText("当前进度：0.00%");
			}
		});
		ImageView preImageView = (ImageView)foldMenu.findViewWithTag("Previous");
		preImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadPreviousChapter();
				TextView progressText = (TextView)findViewById(R.id.progress);
				progressText.setText("当前进度：0.00%");
			}
		});
		ImageView moreImageView = (ImageView)foldMenu.findViewWithTag("More");
		moreImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(self, SettingsActivity.class);
				startActivity(intent);
			}
		});
		
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
				
				Library.getBook(bookname).getDossier(dossiername).getChapterContent(chaptertitle).setProgress(progress);
				Library.setTempBook(Library.getBook(bookname));
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
				
				Library.getBook(bookname).getDossier(dossiername).getChapterContent(chaptertitle).setProgress(progress);
				Library.setTempBook(Library.getBook(bookname));
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
		progress = 0;
    	readingContent.removeAllViews();
    	chapterId = chapterId + 1;
    	chaptertitle = ((ChapterContent)Library.getTempBook().getDossier(dossiername).getChapterContents().get(chapterId)).getChaptertitle();
    	contents = ((ChapterContent)Library.getTempBook().getDossier(dossiername).getChapterContents().get(chapterId));
    	Library.getBook(bookname).getDossier(dossiername).setLastRead(chapterId);
    	Library.setTempBook(Library.getBook(bookname));
    	if(contents.getContents().size() == 0){
    		new TempTask().execute("");
    	}else{
    		TextView chapterTitleText = (TextView)findViewById(R.id.chaptertitle);
    		chapterTitleText.setText(chaptertitle);
    		List<String> contentList = contents.getContents();
    		List<String> imageList = contents.getImageList();
    		for(int i = 0; i < contentList.size(); i ++){
    			List<String> dividedText = contentDivider(contentList.get(i), cLines, cLetters);
    			for(String text : dividedText){
    				MyTextView contentTextView = new MyTextView(this);
    				contentTextView.setTextSize(Config.getFontsize());
    				contentTextView.setLineSpacing(3.0f, Config.getLinespace());
    				contentTextView.setText(text);
    				readingContent.addView(contentTextView);
    			}
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
		progress = 0;
		readingContent.removeAllViews();
    	chapterId = chapterId - 1;
    	chaptertitle = ((ChapterContent)Library.getTempBook().getDossier(dossiername).getChapterContents().get(chapterId)).getChaptertitle();
    	contents = ((ChapterContent)Library.getTempBook().getDossier(dossiername).getChapterContents().get(chapterId));
    	Library.getBook(bookname).getDossier(dossiername).setLastRead(chapterId);
    	Library.setTempBook(Library.getBook(bookname));
    	if(contents.getContents().size() == 0){
    		new TempTask().execute("");
    	}else{
    		TextView chapterTitleText = (TextView)findViewById(R.id.chaptertitle);
    		chapterTitleText.setText(chaptertitle);
    		List<String> contentList = contents.getContents();
    		List<String> imageList = contents.getImageList();
    		for(int i = 0; i < contentList.size(); i ++){
    			List<String> dividedText = contentDivider(contentList.get(i), cLines, cLetters);
    			for(String text : dividedText){
    				MyTextView contentTextView = new MyTextView(this);
    				contentTextView.setTextSize(Config.getFontsize());
    				contentTextView.setLineSpacing(3.0f, Config.getLinespace());
    				contentTextView.setText(text);
    				readingContent.addView(contentTextView);
    			}
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

		private ProgressDialog pd;
		
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
	    			List<String> dividedText = contentDivider(contentList.get(i), cLines, cLetters);
	    			for(String text : dividedText){
	    				MyTextView contentTextView = new MyTextView(self);
	    				contentTextView.setTextSize(Config.getFontsize());
	    				contentTextView.setLineSpacing(3.0f, Config.getLinespace());
	    				contentTextView.setText(text);
	    				readingContent.addView(contentTextView);
	    			}
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
			pd.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = ProgressDialog.show(self, "", "加载中，请稍后……", true, false);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		BookParser parser = new BookParser();
		FileOperator operator = new FileOperator();
		try {
			operator.writeFile("Books", bookname + ".txt", parser.serialize(Library.getBook(bookname)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(timeReceiver);
		super.onDestroy();
	}
	
	//获取可以显示的行数
	private int countLines(){
		int lines = 0;
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); 		// 屏幕高
		double contentHeight = screenHeight * 0.92;
		TextView tempTextView = new MyTextView(self);
		tempTextView.setTextSize(Config.getFontsize());
		tempTextView.setLineSpacing(3.0f, Config.getLinespace());
		lines = (int)(contentHeight / tempTextView.getLineHeight());
		Toast.makeText(this, lines + "", Toast.LENGTH_SHORT).show();
		return lines;
	}
	//获得一行显示的字数
	private int countLetters(){
		int letters = 0;
		int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();  		// 屏幕宽  
		double contentWidth = screenWidth - 10 * getResources().getDisplayMetrics().density;
		TextView tempTextView = new MyTextView(self);
		tempTextView.setTextSize(Config.getFontsize());
		tempTextView.setLineSpacing(3.0f, Config.getLinespace());
		letters = (int)(contentWidth / tempTextView.getTextSize());
		Toast.makeText(this, letters + "", Toast.LENGTH_SHORT).show();
		return letters;
	}
	//处理字符串，增加缩进
	private String addTab(String text){
		String newText = new String();
		String[] tempString = text.toString().split("\n");
		for(int i = 0; i < tempString.length; i ++){
			tempString[i] = "　　" + tempString[i];
			newText = newText + tempString[i] + "\n";
		}
		return newText;
	}
	//将内容分割成一整页能显示的大小
	private List<String> contentDivider(String contentString,int cline, int cletter){
		List<String> result = new ArrayList<String>();
		String tempString = new String();
		String originalString = addTab(contentString);
		String[] originalStrings = originalString.split("\n");
		TextView tempTextView = new MyTextView(self);
		tempTextView.setTextSize(Config.getFontsize());
		tempTextView.setLineSpacing(3.0f, Config.getLinespace());
		float contentWidth = cletter * tempTextView.getTextSize();
		TextPaint mPaint = tempTextView.getPaint();
		
		int i = 0;
		for(int j = 0; j < originalStrings.length ; j ++){
			originalStrings[j] = originalStrings[j] + "\n";
			while(originalStrings[j].length() != 0){
				i ++;
				int count = mPaint.breakText(originalStrings[j], true, contentWidth, null);
				tempString = tempString + originalStrings[j].substring(0, count);
				originalStrings[j] = originalStrings[j].substring(count);
				if(i == cline){
					i = 0;
					result.add(tempString);
					tempString = "";
				}
			}
		}
		if(tempString.length() > 0){
			result.add(tempString);
		}
		
		return result;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(wakeLock != null){
			wakeLock.release();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(wakeLock != null){
			wakeLock.acquire();
		}
	}
	
	
}