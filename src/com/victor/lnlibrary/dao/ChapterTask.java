package com.victor.lnlibrary.dao;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.victor.lnlibrary.ReadingActivity;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.htmlparser.Chapter;
import com.victor.lnlibrary.ui.Expand_Custom_Animation;

public class ChapterTask extends AsyncTask<String, Integer, String>{
	private String bookname;
	private Chapter chapters = null;
	private String link;
	private Activity mActivity;
	private LinearLayout mListView;
	private String title;

	public ChapterTask(Activity activity, String dossierlink, String dossiername, LinearLayout expandLayout, String bookname){
		mActivity = activity;
    	link = dossierlink;
    	mListView = expandLayout;
    	title = dossiername;
    	this.bookname = bookname;
	}

	
	
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try{
			if(mListView.getVisibility() != 0){
				chapters = new Chapter(link);
				return "success";
			}else{
				return "close";
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return e.toString();
		}
	}
	
	
	
	//protected void onPostExecute(String paramString)
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(result.equals("success")){
			Library.getBook(bookname).getDossier(title).setChapters(chapters.getChapterList());
			Library.getTempBook().getDossier(title).setChapters(chapters.getChapterList());
			mListView.removeAllViews();
			for(int i = 0; i < chapters.getChapterList().size(); i ++){
				final TextView chapterText = new TextView(mActivity);
			    chapterText.setText(chapters.getChapterList().get(i));
			    chapterText.setGravity(Gravity.CENTER);
			    mListView.addView(chapterText);
			    chapterText.setOnClickListener(new OnClickListener(){
			    	public void onClick(View v){
			    		if(Library.getBook(bookname).isSaved()){
			    			if(Library.getBook(bookname).getDossier(title).isDownloaded()){
			    				Intent intent = new Intent();
			    				intent.setClass(mActivity, ReadingActivity.class);
			    				intent.putExtra("bookname", bookname);
			    				intent.putExtra("dossiername", title);
			    				intent.putExtra("chapter", chapterText.getText().toString());
			    				mActivity.startActivity(intent);
			    			}else{
			    				new TempChapterTask(mActivity, bookname, title, chapterText.getText().toString()).execute("");
			    			}
			    		}else{
			    			new TempChapterTask(mActivity, bookname, title, chapterText.getText().toString()).execute("");
			    		}
			    	}
			    });	
			}
			Expand_Custom_Animation animation = new Expand_Custom_Animation(mListView, 500);
	        mListView.startAnimation(animation);
		}else if(result.equals("close")){
			Expand_Custom_Animation animation = new Expand_Custom_Animation(mListView, 500);
		    mListView.startAnimation(animation);
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