package com.victor.lnlibrary.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.ReadingActivity;
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
    			Drawable drawable = new BitmapDrawable(imageOperator.loadImage(book + "/dossier-" + String.valueOf(position)));
        		
        		WindowManager manager = (WindowManager)mActivity.getSystemService(Context.WINDOW_SERVICE);
            	DisplayMetrics dm = new DisplayMetrics();
            	manager.getDefaultDisplay().getMetrics(dm);
            	int width = dm.widthPixels * 1 / 6;
            
            	int height = (int) ((float) width/drawable.getMinimumWidth() * drawable.getMinimumHeight());
            	LayoutParams params = new LayoutParams(0, height, 1);
            	params.setMargins(0, 0, (int)(8 * mActivity.getResources().getDisplayMetrics().density), 1);
            	dossiercover.setLayoutParams(params);
            	dossiercover.setImageDrawable(drawable);
    		}else{
    			new ImageLoadTask(mActivity, dossiercover, imagelinkList.get(position), book, "dossier-" + position).execute("");
    		}
    		TextView dossiername = (TextView)convertView.findViewById(R.id.title);
    		dossiername.setText(title.get(position));
    		Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.slide_down);
    		convertView.setAnimation(animation);
    		listMap.put(position, convertView);
    	}else{
    		convertView = listMap.get(position);
    	}
    
    	
    	
    	TextView download = (TextView)convertView.findViewById(R.id.download);
    	TextView readTag = (TextView)convertView.findViewById(R.id.readtag);
    	if(Library.isInLibrary(book)){
    		if(Library.getTempBook().getDossier(title.get(position)).isDownloaded()){
    			download.setText("删除");
    			download.setTextColor(Color.WHITE);
    			download.setClickable(true);
    			ProgressBar progress = (ProgressBar)convertView.findViewById(R.id.progressbar);
    			progress.setProgress(100);
    			final int lastread = Library.getTempBook().getDossier(title.get(position)).getLastRead();
    			if(lastread < 0){
    				readTag.setText("从未读过");
    				readTag.setTextColor(Color.GRAY);
    				readTag.setClickable(false);
    			}else{
    				final String dossiername = title.get(position);
    				readTag.setText("继续阅读");
    				readTag.setClickable(true);
    				readTag.setBackgroundResource(R.drawable.buttonselector);
    				readTag.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.setClass(mActivity, ReadingActivity.class);
							intent.putExtra("bookname", book);
		    				intent.putExtra("dossiername", dossiername);
		    				intent.putExtra("chapter", Library.getTempBook().getDossier(dossiername).getChapterContents().get(lastread).getChaptertitle());
		    				mActivity.startActivity(intent);
						}
					});
    			}
    			
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