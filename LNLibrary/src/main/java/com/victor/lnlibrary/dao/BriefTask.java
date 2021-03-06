package com.victor.lnlibrary.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.htmlparser.BookBrief;
import com.victor.lnlibrary.ui.BookLayout;
import java.util.ArrayList;
import java.util.List;

public class BriefTask extends AsyncTask<String, Integer, String>{
	private List<String> bookList = new ArrayList<String>();
	private List<BookBrief> briefs = null;
	private List<String> linkList = new ArrayList<String>();
	private Activity mActivity;
	private LinearLayout mLayout;
	private ProgressDialog pd;

	public BriefTask(Activity activity, LinearLayout layout, List<String> books, List<String> links, ProgressDialog p){
    	mLayout = layout;
    	mActivity = activity;
    	bookList = books;
    	linkList = links;
    	pd = p;
	}

	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		try{
			briefs = new ArrayList<BookBrief>();
			for(int i = 0; i < bookList.size(); i ++){
				BookBrief bookBrief = new BookBrief(linkList.get(i), bookList.get(i));
				briefs.add(bookBrief);
			}
			return "completed";
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return e.toString();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		//mLayout.removeViewAt(0);
		
		if(result.equals("completed")){
			if(briefs.size() != 0){
				for(BookBrief brief : briefs){
					BookLayout bookLayout = new BookLayout(mActivity, brief);
				    bookLayout.setVisibility(4);
				    mLayout.addView(bookLayout);
				    Animation localAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.slide_down);
				    bookLayout.setVisibility(0);
				    bookLayout.startAnimation(localAnimation);
				}
			}else{
				Toast.makeText(mActivity, "empty", Toast.LENGTH_SHORT).show();
			}
				
		}else{
			Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
		}
		pd.dismiss();
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