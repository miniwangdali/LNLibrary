package com.victor.lnlibrary.dao;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.book.BookParser;
import com.victor.lnlibrary.book.ChapterContent;
import com.victor.lnlibrary.book.Dossier;
import com.victor.lnlibrary.book.FileOperator;
import com.victor.lnlibrary.book.ImageOperator;
import com.victor.lnlibrary.htmlparser.Chapter;
import com.victor.lnlibrary.htmlparser.Content;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DownloadTask extends AsyncTask<String, Integer, String>{
	private String bookname;
	private List<String> chapterimageList;
	private List<String> chapterlinkList;
	private List<ChapterContent> chapters = null;
	private String link;
	private Activity mActivity;
	private Dossier mDossier;
	private ProgressBar mProgressBar;
	private List<String> title;

	public DownloadTask(Activity activity, Dossier dossier, String bookname, String dossierlink, ProgressBar progressBar){
		mActivity = activity;
		this.bookname = bookname;
    	link = dossierlink;
    	mDossier = dossier;
    	mProgressBar = progressBar;
   	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try{
			Chapter chapter = new Chapter(link);
		    publishProgress(2);
		    mDossier.setChapters(chapter.getChapterList());
		    title = chapter.getChapterList();
		    chapterlinkList = chapter.getLinkList();
		    chapters = new ArrayList<ChapterContent>();
			for(int i = 0; i < title.size(); i ++){
				ChapterContent chapterContent = new ChapterContent();
		        chapterContent.setChaptertitle(title.get(i));
		        chapterContent.setChapterlink(chapterlinkList.get(i));
		        Content content = new Content(chapterlinkList.get(i));
		        chapterContent.setContents(Arrays.asList(content.getText().split("@img@")));
		        
		        chapterimageList = new ArrayList<String>();
		        publishProgress(90 / title.size() * (i + 1));
		        for(int j = 0; j < content.getLinkList().size(); j ++){
		        	ImageOperator operator = new ImageOperator();
			        operator.saveImage(operator.downloadImage(content.getLinkList().get(j)), bookname, mDossier.getDossiertitle() + "chapter" + String.valueOf(i) + "-" + String.valueOf(j));
			        String str = bookname + "/" + mDossier.getDossiertitle() + "chapter" + String.valueOf(i) + "-" + String.valueOf(j);
			        chapterimageList.add(str);
			        
		        }
		        chapterContent.setImageList(chapterimageList);
		        chapters.add(chapterContent);
		        publishProgress(90 / title.size() * (i + 1) + 45 / title.size());
			}
			mDossier.setChapterContents(chapters);
			mDossier.setDownloaded(true);
			Library.setTempBook(Library.getBook(bookname));
			publishProgress(100);
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
			try{
				BookParser parser = new BookParser();
				FileOperator operator = new FileOperator();
			    operator.writeFile("Books", bookname + ".txt", parser.serialize(Library.getBook(this.bookname)));
			    ((BaseAdapter)((ListView)mActivity.findViewById(R.id.dossierlist)).getAdapter()).notifyDataSetChanged();
			    
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(mActivity, "下载失败", Toast.LENGTH_SHORT).show();
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
  		mProgressBar.setProgress(values[0]);
  		super.onProgressUpdate(values);
  	}

}