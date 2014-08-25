package com.victor.lnlibrary.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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
    	bookcover.setImageDrawable(((ImageView)bookLayout.findViewById(R.id.bookcover)).getDrawable());
    	
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
    	if(operator.isFileExist("Images/" + mBook.getTitle() + "/bookcover" + ".png")){
    		ImageOperator imageOperator = new ImageOperator();
    		bookcover.setImageBitmap(imageOperator.loadImage(mBook.getTitle() + "/bookcover"));
    	}
    
    super.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        FragmentTransaction localFragmentTransaction = ((Activity)BookLayout.this.getContext()).getFragmentManager().beginTransaction();
        Fragment_BookDetail localFragment_BookDetail = new Fragment_BookDetail();
        localFragment_BookDetail.setBookLayout(BookLayout.this.self);
        localFragment_BookDetail.setTitle(BookLayout.this.mBook.getTitle());
        localFragment_BookDetail.setLink(BookLayout.this.mBook.getBookLink());
        localFragmentTransaction.replace(2131165187, localFragment_BookDetail);
        localFragmentTransaction.addToBackStack("list");
        localFragmentTransaction.commit();
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
    	if(operator.isFileExist("Images/" + mBook.getTitle() + "/bookcover" + ".png")){
    		ImageOperator imageOperator = new ImageOperator();
    		bookcover.setImageBitmap(imageOperator.loadImage(mBook.getTitle() + "/bookcover"));
    	}else{
    		new ImageLoadTask((Activity)getContext(), bookcover, bookBrief.getImageLink(), bookBrief.getTitle(), "bookcover").execute("");
    	}
    super.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        FragmentTransaction localFragmentTransaction = ((Activity)BookLayout.this.getContext()).getFragmentManager().beginTransaction();
        Fragment_BookDetail localFragment_BookDetail = new Fragment_BookDetail();
        localFragment_BookDetail.setBookLayout(BookLayout.this.self);
        localFragment_BookDetail.setTitle(BookLayout.this.details.getTitle());
        localFragment_BookDetail.setLink(BookLayout.this.details.getBooklink());
        localFragmentTransaction.replace(2131165187, localFragment_BookDetail);
        localFragmentTransaction.addToBackStack("list");
        localFragmentTransaction.commit();
      }
    });
    
	}
	

  	public BookBrief getDetails(){
  		return details;
  	}
}