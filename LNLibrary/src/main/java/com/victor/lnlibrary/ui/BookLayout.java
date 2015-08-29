package com.victor.lnlibrary.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.Fragment_BookDetail;
import com.victor.lnlibrary.book.Book;
import com.victor.lnlibrary.book.FileOperator;
import com.victor.lnlibrary.book.ImageOperator;
import com.victor.lnlibrary.dao.ImageLoadTask;
import com.victor.lnlibrary.htmlparser.BookBrief;

public class BookLayout extends LinearLayout{
	private Book mBook;
	private BookBrief details;
	private BookLayout self;

	public BookLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BookLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BookLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BookLayout(Context context, Book book){
		super(context);
		((Activity)getContext()).getLayoutInflater().inflate(R.layout.layout_book, this);
		self = this;
		mBook = book;
		if(mBook != null){
			initialize(mBook);
		}
	}
	
	public BookLayout(Context context, BookBrief bookBrief){
		super(context);
		((Activity)getContext()).getLayoutInflater().inflate(R.layout.layout_book, this);
		self = this;
		details = bookBrief;
		if(details != null){
			initialize(details);
		}
	}

	public BookLayout(BookLayout bookLayout){
		super(bookLayout.getContext());
		((Activity)getContext()).getLayoutInflater().inflate(R.layout.layout_book, this);
    	self = this;
    	TextView bookname = (TextView)findViewById(R.id.bookname);   
    	TextView author = (TextView)findViewById(R.id.author);
    	TextView illustrator = (TextView)findViewById(R.id.illustrator);
    	TextView publisher = (TextView)findViewById(R.id.publisher);
    	TextView newest = (TextView)findViewById(R.id.newest);
    	TextView updatetime = (TextView)findViewById(R.id.updatetime);
    	
    	bookname.setText(((TextView)bookLayout.findViewById(R.id.bookname)).getText().toString());
    	author.setText(((TextView)bookLayout.findViewById(R.id.author)).getText().toString());
    	illustrator.setText(((TextView)bookLayout.findViewById(R.id.illustrator)).getText().toString());
    	publisher.setText(((TextView)bookLayout.findViewById(R.id.publisher)).getText().toString());
    	newest.setText(((TextView)bookLayout.findViewById(R.id.newest)).getText().toString());
    	updatetime.setText(((TextView)bookLayout.findViewById(R.id.updatetime)).getText().toString());
    	
    	ImageView bookcover = (ImageView)findViewById(R.id.bookcover);
    	
    	WindowManager manager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
    	DisplayMetrics dm = new DisplayMetrics();
    	manager.getDefaultDisplay().getMetrics(dm);
    	int width = dm.widthPixels * 2 / 9;
    	
    	Drawable drawable = ((ImageView)bookLayout.findViewById(R.id.bookcover)).getDrawable();
    	int height = (int) ((float) width/drawable.getMinimumWidth() * drawable.getMinimumHeight());
    	LayoutParams params = new LayoutParams(0, height, 2.0f);
    	params.setMargins(0, 0, (int)(10 * getContext().getResources().getDisplayMetrics().density), 1);
    	bookcover.setLayoutParams(params);
    	bookcover.setImageDrawable(drawable);
    	
	}
	
	private void initialize(Book book){
		TextView bookname = (TextView)findViewById(R.id.bookname);   
    	TextView author = (TextView)findViewById(R.id.author);
    	TextView illustrator = (TextView)findViewById(R.id.illustrator);
    	TextView publisher = (TextView)findViewById(R.id.publisher);
    	TextView newest = (TextView)findViewById(R.id.newest);
    	TextView updatetime = (TextView)findViewById(R.id.updatetime);
    	
    	bookname.setText(book.getTitle());
    	author.setText(book.getAuthor());
    	illustrator.setText(book.getIllustrator());
    	publisher.setText(book.getPublisher());
    	newest.setText(book.getNewest());
    	updatetime.setText(book.getUpdatetime());
    	
    	ImageView bookcover = (ImageView)findViewById(R.id.bookcover);
    	FileOperator operator = new FileOperator();
    	if(operator.isFileExist("Images/" + mBook.getTitle() + "/bookcover" + ".ill")){
    		ImageOperator imageOperator = new ImageOperator();
    		Drawable drawable = new BitmapDrawable(imageOperator.loadImage(book.getTitle() + "/bookcover"));
    		
    		WindowManager manager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        	DisplayMetrics dm = new DisplayMetrics();
        	manager.getDefaultDisplay().getMetrics(dm);
        	int width = dm.widthPixels * 2 / 9;
        
        	int height = (int) ((float) width/drawable.getMinimumWidth() * drawable.getMinimumHeight());
        	LayoutParams params = new LayoutParams(0, height, 2.0f);
        	params.setMargins(0, 0, (int)(10 * getContext().getResources().getDisplayMetrics().density), 1);
        	bookcover.setLayoutParams(params);
        	bookcover.setImageDrawable(drawable);
    	}
    
    	setOnClickListener(new View.OnClickListener(){
    		@Override
    		public void onClick(View paramView){
    			FragmentTransaction ft = ((Activity)getContext()).getFragmentManager().beginTransaction();
    			Fragment_BookDetail fragment_BookDetail = new Fragment_BookDetail();
    			fragment_BookDetail.setBookLayout(self);
    			fragment_BookDetail.setTitle(mBook.getTitle());
    			fragment_BookDetail.setLink(mBook.getBookLink());
    			ft.replace(R.id.fragment_home, fragment_BookDetail);
    			ft.addToBackStack("list");
    			ft.commit();
    		}
    	});

  }

	private void initialize(BookBrief bookBrief){
		TextView bookname = (TextView)findViewById(R.id.bookname);   
    	TextView author = (TextView)findViewById(R.id.author);
    	TextView illustrator = (TextView)findViewById(R.id.illustrator);
    	TextView publisher = (TextView)findViewById(R.id.publisher);
    	TextView newest = (TextView)findViewById(R.id.newest);
    	TextView updatetime = (TextView)findViewById(R.id.updatetime);
    	
    	bookname.setText(bookBrief.getTitle());
    	author.setText(bookBrief.getAuthor());
    	illustrator.setText(bookBrief.getIllustrator());
    	publisher.setText(bookBrief.getPublisher());
    	newest.setText(bookBrief.getNewest());
    	updatetime.setText(bookBrief.getUpdatetime());
    	
    	ImageView bookcover = (ImageView)findViewById(R.id.bookcover);
    	FileOperator operator = new FileOperator();
    	if(operator.isFileExist("Images/" + bookBrief.getTitle() + "/bookcover" + ".ill")){
    		ImageOperator imageOperator = new ImageOperator();
    		Drawable drawable = new BitmapDrawable(imageOperator.loadImage(bookBrief.getTitle() + "/bookcover"));
    		
    		WindowManager manager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        	DisplayMetrics dm = new DisplayMetrics();
        	manager.getDefaultDisplay().getMetrics(dm);
        	int width = dm.widthPixels * 2 / 9;
        
        	int height = (int) ((float) width/drawable.getMinimumWidth() * drawable.getMinimumHeight());
        	LayoutParams params = new LayoutParams(0, height, 2.0f);
        	params.setMargins(0, 0, (int)(10 * getContext().getResources().getDisplayMetrics().density), 1);
        	bookcover.setLayoutParams(params);
        	bookcover.setImageDrawable(drawable);
    	}else{
    		if(bookBrief.getImageLink() != null){
    			ImageLoadTask mImageLoadTask = new ImageLoadTask((Activity)getContext(), bookcover, bookBrief.getImageLink(), bookBrief.getTitle(), "bookcover");
        		mImageLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
			}else{
				bookcover.setImageResource(R.drawable.image_load_error);
			}
    		ImageLoadTask mImageLoadTask = new ImageLoadTask((Activity)getContext(), bookcover, bookBrief.getImageLink(), bookBrief.getTitle(), "bookcover");
    		mImageLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    	}
    	setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View paramView){
    			FragmentTransaction ft = ((Activity)getContext()).getFragmentManager().beginTransaction();
    			Fragment_BookDetail fragment_BookDetail = new Fragment_BookDetail();
    			fragment_BookDetail.setBookLayout(self);
    			fragment_BookDetail.setTitle(details.getTitle());
    			fragment_BookDetail.setLink(details.getBooklink());
    			ft.replace(R.id.fragment_home, fragment_BookDetail);
    			ft.addToBackStack("list");
    			ft.commit();
    		}
    	});
    
	}
	

  	public BookBrief getDetails(){
  		return details;
  	}
}