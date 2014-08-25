package com.victor.lnlibrary.ui;

import android.app.Activity;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.book.Dossier;
import com.victor.lnlibrary.book.FileOperator;
import com.victor.lnlibrary.book.ImageOperator;
import com.victor.lnlibrary.config.StaticConfig;
import com.victor.lnlibrary.dao.DownloadTask;
import com.victor.lnlibrary.dao.ImageLoadTask;
import java.util.List;

public class DossierListAdapter extends BaseAdapter{
	private String book;
	private List<String> imagelinkList;
	private List<String> links;
	private SparseArray<View> listMap;
	private Activity mActivity;
	private List<String> title;
	
	public DossierListAdapter(Activity activity, String bookname){
    	mActivity = activity;
    	book = bookname;
    	listMap = new SparseArray<View>();
    }
	
	public int getCount(){
		return title.size();
	}

	public List<String> getImagelinkList(){
		return imagelinkList;
	}

	public Object getItem(int paramInt){
		return this.title.get(paramInt);
	}

	public long getItemId(int paramInt){
		return 0;
	}

	public List<String> getLinks(){
		return links;
	}

	public View getView(int position, View convertView, ViewGroup parent){
		
    	if(listMap.get(position) == null){
    		convertView = mActivity.getLayoutInflater().inflate(R.layout.dossierlist_item, null);
    		ImageView dossiercover = (ImageView)convertView.findViewById(R.id.cover);
    		FileOperator operator = new FileOperator();
    		if(operator.isFileExist("Images/" + book + "/dossier-" + position + ".png")){
    			ImageOperator imageOperator = new ImageOperator();
    			dossiercover.setImageBitmap(imageOperator.loadImage(book + "/dossier-" + String.valueOf(position)));
    		}else{
    			new ImageLoadTask(mActivity, dossiercover, imagelinkList.get(position), book, "dossier-" + position).execute("");
    		}
    		TextView dossiername = (TextView)convertView.findViewById(R.id.title);
    		dossiername.setText(title.get(position));
    		//label136: ((TextView)convertView.findViewById(R.id.title)).setText(title.get(position));
    		Animation animation = AnimationUtils.loadAnimation(mActivity, R.id.dossierlist);
    		convertView.setAnimation(animation);
    		listMap.put(position, convertView);
    	}else{
    		convertView = listMap.get(position);
    	}
    
    	
    	
    	TextView download = (TextView)convertView.findViewById(R.id.download);
    	if(Library.isInLibrary(book)){
    		if(Library.getTempBook().getDossier(title.get(position)).isDownloaded()){
    			download.setText("删除");
    			download.setTextColor(Color.WHITE);
    			download.setClickable(true);
    			ProgressBar progress = (ProgressBar)convertView.findViewById(R.id.progressbar);
    			progress.setProgress(100);
    		}else{
    			download.setText("下载");
    			download.setTextColor(Color.BLACK);
    			download.setClickable(true);
    			
    			final String dossiername = title.get(position);
    		    final String chapterlink = links.get(position);
    			
    			final ProgressBar progress = (ProgressBar)convertView.findViewById(R.id.progressbar);
    			progress.setProgress(0);
    			download.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(StaticConfig.hasInternet(mActivity)){
							try{
								Dossier dossier = Library.getBook(book).getDossier(dossiername);
					            new DownloadTask(mActivity, dossier, book, chapterlink, progress).execute("");
					        }catch(Exception e){
					            Library.getBook(book).getDossier(dossiername).setDownloaded(false);
					            Library.getTempBook().getDossier(dossiername).setDownloaded(false);
					            Toast.makeText(mActivity, "下载失败", Toast.LENGTH_SHORT).show();
					        }
						}else{
					        Toast.makeText(DossierListAdapter.this.mActivity, "无网络连接", Toast.LENGTH_SHORT).show();
					    }
					}
				});
    		}
    	}else{
    		download.setText("请先收藏");
    		download.setTextColor(Color.BLACK);
    		download.setClickable(false);
    		ProgressBar progress = (ProgressBar)convertView.findViewById(R.id.progressbar);
    		progress.setProgress(0);
    	}
    	
    	return convertView;
	}

	public void setImagelinkList(List<String> imageLinks){
    	imagelinkList = imageLinks;
	}

	public void setLinks(List<String> linkList){
    	links = linkList;
	}

	public void setTitle(List<String> dossierList){
    	title = dossierList;
	}
}