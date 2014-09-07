package com.victor.lnlibrary.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.victor.lnlibrary.ReadingActivity;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.book.ChapterContent;
import com.victor.lnlibrary.htmlparser.Content;
import java.util.Arrays;
import java.util.List;

public class TempChapterTask extends AsyncTask<String, Integer, String>{
	private String bookname;
	private ChapterContent chapterContent;
	private String chaptertitle;
	private String dossiername;
	private Activity mActivity;
	private ProgressDialog pd;
	
	public TempChapterTask(Activity activity, String bookname, String dossiername, String chaptertitle){
		mActivity = activity;
		this.bookname = bookname;
		this.dossiername = dossiername;
		this.chaptertitle = chaptertitle;
	}

	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try{
			chapterContent = Library.getTempBook().getDossier(dossiername).getChapterContent(chaptertitle);
		    Content content = new Content(chapterContent.getChapterlink());
		    List<String> texts = Arrays.asList(content.getText().split("@img@"));
		    List<String> images = content.getLinkList();
		    chapterContent.setContents(texts);
		    chapterContent.setImageList(images);
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
			Intent intent = new Intent();
		    intent.setClass(mActivity, ReadingActivity.class);
		    intent.putExtra("bookname", bookname);
		    intent.putExtra("dossiername", dossiername);
		    intent.putExtra("chapter", chaptertitle);
		    mActivity.startActivity(intent);
		}else{
			Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
		}
		pd.dismiss();
		super.onPostExecute(result);
	}


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		pd = ProgressDialog.show(mActivity, "", "加载中，请稍后……", true, false);
		super.onPreExecute();
	}


	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}