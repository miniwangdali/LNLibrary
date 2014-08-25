package com.victor.lnlibrary.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.Fragment_BookList;

public class BlockLayout extends LinearLayout{
	private String title;

	
	public BlockLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BlockLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BlockLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BlockLayout(Context context, String blockname){
		super(context);
		((Activity)getContext()).getLayoutInflater().inflate(R.layout.layout_block, this);
		title = blockname;
		initialize(blockname);
	}

	private void initialize(String blockname){
		TextView name = (TextView)findViewById(R.id.blockname);
		name.setText(blockname);
		name.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(title.equals("我的书库")){
					FragmentManager fragmentManager = ((Activity)getContext()).getFragmentManager();
					FragmentTransaction fTransaction = fragmentManager.beginTransaction();
					Fragment_BookList fragment_BookList = new Fragment_BookList();
					fragment_BookList.setTitle(title);
					fragment_BookList.setCommand("mylibrary");
					fTransaction.replace(R.id.fragment_home, fragment_BookList);
					fTransaction.addToBackStack("home");
					fTransaction.commit();
				}else{
					FragmentManager fragmentManager = ((Activity)getContext()).getFragmentManager();
					FragmentTransaction fTransaction = fragmentManager.beginTransaction();
					Fragment_BookList fragment_BookList = new Fragment_BookList();
					fragment_BookList.setTitle(title);
					fragment_BookList.setCommand("block");
					fTransaction.replace(R.id.fragment_home, fragment_BookList);
					fTransaction.addToBackStack("home");
					fTransaction.commit();
				}
				
			}
		});
	}
}