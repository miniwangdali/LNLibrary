package com.victor.lnlibrary.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.book.Book;
import com.victor.lnlibrary.config.StaticConfig;
import com.victor.lnlibrary.htmlparser.BlockItems;
import com.victor.lnlibrary.htmlparser.SearchItems;
import com.victor.lnlibrary.ui.BookLayout;
import java.util.List;

public class FuncTask extends AsyncTask<String, Integer, String>{
	private List<Book> bookList;
	private String command;
	private Activity mActivity;
	private LinearLayout mLayout;
	private String param;

	public FuncTask(Activity activity, LinearLayout layout, String command, String param){
    	mActivity = activity;
    	mLayout = layout;
    	this.command = command;
    	this.param = param;
	}

	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if(command.equals("mylibrary")){
			return "mylibrary";
		}else{
			if(StaticConfig.hasInternet(mActivity)){
				if(command.equals("block")){
					BlockItems blockItems = new BlockItems(param);
					new BriefTask(mActivity, mLayout, blockItems.getBookList(), blockItems.getBooklinkList()).execute("");
					return "block";
				}else if(command.equals("search")){
					SearchItems searchItems = new SearchItems(param);
					new BriefTask(mActivity, mLayout, searchItems.getBookList(), searchItems.getLinkList()).execute("");
					return "search";
				}else{
					return null;
				}
			}else{
				return "disconnected";
			}
		}
	}
	
	


	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(result.equals("mylibrary")){
			bookList = Library.getMyLibrary();
			for(Book book : bookList){
				BookLayout bookLayout = new BookLayout(mActivity, book);
				bookLayout.setVisibility(View.INVISIBLE);
			    mLayout.addView(bookLayout);
			    Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.slide_down);
			    bookLayout.setVisibility(View.VISIBLE);
			    bookLayout.startAnimation(animation);
			}
		}else if(result.equals("block")){
			
		}else if(result.equals("search")){
			
		}else if(result.equals("disconnected")){
			Toast.makeText(mActivity, "无网络连接", Toast.LENGTH_SHORT).show();
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