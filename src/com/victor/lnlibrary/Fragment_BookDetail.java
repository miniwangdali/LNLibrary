package com.victor.lnlibrary;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.bean.Library;
import com.victor.lnlibrary.book.Book;
import com.victor.lnlibrary.book.BookParser;
import com.victor.lnlibrary.book.Dossier;
import com.victor.lnlibrary.book.FileOperator;
import com.victor.lnlibrary.config.StaticConfig;
import com.victor.lnlibrary.dao.ChapterTask;
import com.victor.lnlibrary.dao.DetailTask;
import com.victor.lnlibrary.ui.BookLayout;
import com.victor.lnlibrary.ui.DossierListAdapter;
import com.victor.lnlibrary.ui.Expand_Custom_Animation;
import com.victor.lnlibrary.ui.ExpandableTextView;
import java.util.ArrayList;
import java.util.List;

public class Fragment_BookDetail extends Fragment{
	private String link;
	private BookLayout mBookLayout;
	private LinearLayout mLayout;
	private String title;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_bookdetail, container, false);
		mLayout = (LinearLayout)view.findViewById(R.id.bookdetail);
		BookLayout bookLayout = new BookLayout(mBookLayout);
		mLayout.addView(bookLayout, 0);
		
		if(StaticConfig.hasInternet(getActivity())){
			new DetailTask(getActivity(), mLayout, title, link).execute("");
			
			if(Library.isInLibrary(title)){
				Book book = Library.getBook(title);
				Library.setTempBook(book);
				Library.getTempBook().setTitle(((TextView)mBookLayout.findViewById(R.id.bookname)).getText().toString());
				Library.getTempBook().setAuthor(((TextView)mBookLayout.findViewById(R.id.author)).getText().toString());
				Library.getTempBook().setIllustrator(((TextView)mBookLayout.findViewById(R.id.illustrator)).getText().toString());
				Library.getTempBook().setPublisher(((TextView)mBookLayout.findViewById(R.id.publisher)).getText().toString());
				Library.getTempBook().setNewest(((TextView)mBookLayout.findViewById(R.id.newest)).getText().toString());
				Library.getTempBook().setUpdatetime(((TextView)mBookLayout.findViewById(R.id.updatetime)).getText().toString());
				Library.getTempBook().setBookLink(link);
				TextView save = (TextView)view.findViewById(R.id.save);
				save.setText("已收藏");
				save.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try{
							Library.getTempBook().setSaved(true);
							Library.addBook(Library.getTempBook());
							BookParser parser = new BookParser();
							FileOperator operator = new FileOperator();
							if (operator.writeFile("Books", title + ".txt", parser.serialize(Library.getTempBook()))){
								((BaseAdapter)((ListView)mLayout.findViewById(R.id.dossierlist)).getAdapter()).notifyDataSetChanged();
							}	
						}
						catch (Exception e){
							e.printStackTrace();
							Library.getTempBook().setSaved(false);
							Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
						}
					}
				});
			}else{
				Library.setTempBook(new Book());
				Library.getTempBook().setTitle(((TextView)mBookLayout.findViewById(R.id.bookname)).getText().toString());
				Library.getTempBook().setAuthor(((TextView)mBookLayout.findViewById(R.id.author)).getText().toString());
				Library.getTempBook().setIllustrator(((TextView)mBookLayout.findViewById(R.id.illustrator)).getText().toString());
				Library.getTempBook().setPublisher(((TextView)mBookLayout.findViewById(R.id.publisher)).getText().toString());
				Library.getTempBook().setNewest(((TextView)mBookLayout.findViewById(R.id.newest)).getText().toString());
				Library.getTempBook().setUpdatetime(((TextView)mBookLayout.findViewById(R.id.updatetime)).getText().toString());
				Library.getTempBook().setBookLink(link);
				final TextView save = (TextView)view.findViewById(R.id.save);
				save.setText("收藏");
				save.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try{
							Library.getTempBook().setSaved(true);
							Library.addBook(Library.getTempBook());
							BookParser parser = new BookParser();
							FileOperator operator = new FileOperator();
							if (operator.writeFile("Books", title + ".txt", parser.serialize(Library.getTempBook()))){
								((BaseAdapter)((ListView)mLayout.findViewById(R.id.dossierlist)).getAdapter()).notifyDataSetChanged();
							}	
							save.setText("已收藏");
						}
						catch (Exception e){
							e.printStackTrace();
							Library.getTempBook().setSaved(false);
							Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
			
			
		}else{
			if(Library.isInLibrary(title)){
				Book book = Library.getBook(title);
				Library.setTempBook(book);
				final ExpandableTextView introduction = (ExpandableTextView)mLayout.findViewById(R.id.introduction);
			    introduction.setText(book.getIntroduction());
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
			    DossierListAdapter dossierAdapter = new DossierListAdapter(getActivity(), title);
			    List<String> dossiertitles = new ArrayList<String>();
			    List<String> linkList = new ArrayList<String>();
			    for(Dossier dossier : book.getDossiers()){
			    	dossiertitles.add(dossier.getDossiertitle());
			    	linkList.add(dossier.getDossierLink());
			    }
			    dossierAdapter.setTitle(dossiertitles);
			    dossierAdapter.setLinks(linkList);
			    ListView dossiers = (ListView)view.findViewById(R.id.dossierlist);
			    dossiers.setAdapter(dossierAdapter);
			    dossiers.setOnItemClickListener(new OnItemClickListener(){
			        @Override
			    	public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id){
			        	LinearLayout chapterList = (LinearLayout)view.findViewById(R.id.chapterlist);
			        	if(Library.getTempBook().getDossiers().get(position).getChapterContents().size() != 0){
			        		if (chapterList.getVisibility() != 0){
			        			chapterList.removeAllViews();
			        			for(int i = 0; i < Library.getTempBook().getDossiers().get(position).getChapterContents().size(); i ++){
			        				final TextView chapterText = new TextView(getActivity());
					                chapterText.setText(Library.getTempBook().getDossiers().get(position).getChapters().get(i));
					                chapterText.setGravity(Gravity.CENTER);
					                chapterList.addView(chapterText);
					                chapterText.setOnClickListener(new OnClickListener(){
					                	@Override
					                	public void onClick(View v){
					                    Intent intent = new Intent();
					                    intent.setClass(getActivity(), ReadingActivity.class);
					                    intent.putExtra("bookname", title);
					                    intent.putExtra("dossiername", ((TextView)view.findViewById(R.id.title)).getText().toString());
					                    intent.putExtra("chapter", chapterText.getText().toString());
					                    getActivity().startActivity(intent);
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
			        		new ChapterTask(getActivity(), Library.getTempBook().getDossiers().get(position).getDossierLink(), Library.getTempBook().getDossiers().get(position).getDossiertitle(), chapterList, title).execute("");
			        	}
			        }
			    });
			}else{
				Toast.makeText(getActivity(), "无网络连接", Toast.LENGTH_SHORT).show();
			}
		}
		return view;
	}


  	public void setBookLayout(BookLayout bookLayout){
  		mBookLayout = bookLayout;
  	}

  	public void setLink(String booklink){
  		link = booklink;
  	}

  	public void setTitle(String bookname){
  		title = bookname;
  	}
}