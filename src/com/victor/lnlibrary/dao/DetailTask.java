package com.victor.lnlibrary.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.ReadingActivity;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.book.BookParser;
import com.victor.lnlibrary.book.Dossier;
import com.victor.lnlibrary.book.FileOperator;
import com.victor.lnlibrary.htmlparser.BookDetail;
import com.victor.lnlibrary.ui.DossierListAdapter;
import com.victor.lnlibrary.ui.Expand_Custom_Animation;
import com.victor.lnlibrary.ui.ExpandableTextView;

public class DetailTask extends AsyncTask<String, Integer, String>{
	private BookDetail detail = null;
	private String link;
	private Activity mActivity;
	private LinearLayout mLayout;
	private String name;
	private ProgressDialog pd;

	public DetailTask(Activity activity, LinearLayout layout, String bookname, String booklink){
    	mActivity = activity;
    	mLayout = layout;
    	name = bookname;
    	link = booklink;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try{
			detail = new BookDetail(link, name);
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
			if(Library.isInLibrary(name)){
				final ExpandableTextView introduction = (ExpandableTextView)mLayout.findViewById(R.id.introduction);
			    introduction.setText(detail.getIntroduction());
			    introduction.setTag(R.id.tag_expandable_text_view_reused, new Object());
			    introduction.setExpanded(false);
			    introduction.setOnExpandListener(new ExpandableTextView.OnExpandListener(){
			    	@Override
			    	public void onExpand(ExpandableTextView parent){
			    		introduction.setExpanded(true);
			        }
			    }).setOnCollapseListener(new ExpandableTextView.OnCollapseListener(){
			    	@Override
			        public void onCollapse(ExpandableTextView parent){
			    		introduction.setExpanded(false);
			        }
			    }).setOnClickListener(new OnClickListener(){
			    	@Override
			        public void onClick(View v){
			    		introduction.toggle();
			        }
			    });
			    
			    Library.getBook(name).setIntroduction(detail.getIntroduction());
			    for(int i = 0; i < detail.getDossierList().size(); i ++){
			    	if(Library.getBook(name).getDossier(detail.getDossierList().get(i)) == null){
			    		Dossier dossier = new Dossier();
			    		dossier.setDossiertitle(detail.getDossierList().get(i));
			            dossier.setDossierLink(detail.getLinkList().get(i));
			            Library.getBook(name).getDossiers().add(dossier);
			    	}
			    }
			    Library.setTempBook(Library.getBook(name));
			    
			    ListView dossiers = (ListView)mLayout.findViewById(R.id.dossierlist);
			    DossierListAdapter dossierAdapter = new DossierListAdapter(mActivity, name);
			    dossierAdapter.setTitle(detail.getDossierList());
			    dossierAdapter.setLinks(detail.getLinkList());
			    dossierAdapter.setImagelinkList(detail.getImageLinkList());
			    dossiers.setAdapter(dossierAdapter);
			    dossiers.setOnItemClickListener(new OnItemClickListener(){
			        @Override
			    	public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id){
			        	LinearLayout chapterList = (LinearLayout)view.findViewById(R.id.chapterlist);
			        	if(Library.getBook(name).getDossiers().get(position).getChapterContents().size() != 0){
			        		if (chapterList.getVisibility() != 0){
			        			chapterList.removeAllViews();
			        			for(int i = 0; i < Library.getBook(name).getDossiers().get(position).getChapterContents().size(); i ++){
			        				final TextView chapterText = new TextView(mActivity);
					                chapterText.setText(Library.getBook(name).getDossiers().get(position).getChapters().get(i));
					                chapterText.setGravity(Gravity.CENTER);
					                chapterText.setPadding(0, 10, 0, 10);
					                
					                chapterText.setTextSize(14.0f);
					                chapterText.setBackgroundResource(R.drawable.chapterselector);
					                chapterList.addView(chapterText);
					                chapterText.setOnClickListener(new OnClickListener(){
					                	@Override
					                	public void onClick(View v){
					                    Intent intent = new Intent();
					                    intent.setClass(mActivity, ReadingActivity.class);
					                    intent.putExtra("bookname", name);
					                    intent.putExtra("dossiername", ((TextView)view.findViewById(R.id.title)).getText().toString());
					                    intent.putExtra("chapter", chapterText.getText().toString());
					                    mActivity.startActivity(intent);
					                  }
					                });
					    			
			        			}
			        			Expand_Custom_Animation animation = new Expand_Custom_Animation(chapterList, 500);
				    	        chapterList.startAnimation(animation);
			        		}else{
			        			Expand_Custom_Animation animation = new Expand_Custom_Animation(chapterList, 500);
				    	        chapterList.startAnimation(animation);
			        		}
			        	}else{
			        		ChapterTask mChapterTask = new ChapterTask(mActivity, detail.getLinkList().get(position), detail.getDossierList().get(position), chapterList, name);
			        		mChapterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
			        	}
			        }
			    });
			    
			    BookParser parser = new BookParser();
			    try{
			    	FileOperator operator = new FileOperator();
			    	operator.writeFile("Books", name + ".txt", parser.serialize(Library.getBook(name)));
			    }catch (Exception e) {
					// TODO: handle exception
			    	e.printStackTrace();
			    	Toast.makeText(mActivity, "文件写入错误", Toast.LENGTH_SHORT).show();
				}
			    			    
			}else{
				final ExpandableTextView introduction = (ExpandableTextView)mLayout.findViewById(R.id.introduction);
			    introduction.setText(detail.getIntroduction());
			    introduction.setTag(R.id.tag_expandable_text_view_reused, new Object());
			    introduction.setExpanded(false);
			    introduction.setOnExpandListener(new ExpandableTextView.OnExpandListener(){
			    	@Override
			    	public void onExpand(ExpandableTextView parent){
			    		introduction.setExpanded(true);
			        }
			    }).setOnCollapseListener(new ExpandableTextView.OnCollapseListener(){
			    	@Override
			        public void onCollapse(ExpandableTextView parent){
			    		introduction.setExpanded(false);
			        }
			    }).setOnClickListener(new OnClickListener(){
			    	@Override
			        public void onClick(View v){
			    		introduction.toggle();
			        }
			    });
			    Library.getTempBook().setIntroduction(detail.getIntroduction());
			    for(int i = 0; i < detail.getDossierList().size(); i ++){
			    	Dossier dossier = new Dossier();
				    dossier.setDossiertitle(detail.getDossierList().get(i));
				    dossier.setDossierLink(detail.getLinkList().get(i));
				    Library.getTempBook().getDossiers().add(dossier);
			    }
			    
			    ListView dossiers = (ListView)mLayout.findViewById(R.id.dossierlist);
			    DossierListAdapter dossierAdapter = new DossierListAdapter(mActivity, name);
			    dossierAdapter.setTitle(detail.getDossierList());
			    dossierAdapter.setLinks(detail.getLinkList());
			    dossierAdapter.setImagelinkList(detail.getImageLinkList());
			    dossiers.setAdapter(dossierAdapter);
			    dossiers.setOnItemClickListener(new OnItemClickListener(){
			        @Override
			    	public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id){
			        	LinearLayout chapterList = (LinearLayout)view.findViewById(R.id.chapterlist);
			        	if(Library.getTempBook().getDossiers().get(position).getChapterContents().size() != 0){
			        		if (chapterList.getVisibility() != 0){
			        			chapterList.removeAllViews();
			        			for(int i = 0; i < Library.getTempBook().getDossiers().get(position).getChapterContents().size(); i ++){
			        				final TextView chapterText = new TextView(mActivity);
					                chapterText.setText(Library.getTempBook().getDossiers().get(position).getChapters().get(i));
					                chapterText.setGravity(Gravity.CENTER);
					                chapterText.setPadding(0, 10, 0, 10);
					                
					                chapterText.setTextSize(14.0f);
					                chapterText.setBackgroundResource(R.drawable.chapterselector);
					                
					                chapterList.addView(chapterText);
					                chapterText.setOnClickListener(new OnClickListener(){
					                	@Override
					                	public void onClick(View v){
					                    Intent intent = new Intent();
					                    intent.setClass(mActivity, ReadingActivity.class);
					                    intent.putExtra("bookname", name);
					                    intent.putExtra("dossiername", ((TextView)view.findViewById(R.id.title)).getText().toString());
					                    intent.putExtra("chapter", chapterText.getText().toString());
					                    mActivity.startActivity(intent);
					                  }
					                });
					    			
			        			}
			        			Expand_Custom_Animation animation = new Expand_Custom_Animation(chapterList, 500);
				    	        chapterList.startAnimation(animation);
			        		}else{
			        			Expand_Custom_Animation animation = new Expand_Custom_Animation(chapterList, 500);
				    	        chapterList.startAnimation(animation);
			        		}
			        	}else{
			        		ChapterTask mChapterTask = new ChapterTask(mActivity, detail.getLinkList().get(position), detail.getDossierList().get(position), chapterList, name);
			        		mChapterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
			        	}
			        }
			    });
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
		pd = ProgressDialog.show(mActivity, "", "加载中，请稍后……", true, false);
		if(Library.isInLibrary(name)){
			pd.dismiss();
		}
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
		

}