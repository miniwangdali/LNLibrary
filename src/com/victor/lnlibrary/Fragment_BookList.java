package com.victor.lnlibrary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.dao.FuncTask;

public class Fragment_BookList extends Fragment{
	private String command = new String();
	private LinearLayout mLayout;
	private String title = new String();
	
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_booklist, container, false);
	    mLayout = ((LinearLayout)view.findViewById(R.id.booklistlayout));
	    new FuncTask(getActivity(), mLayout, command, title).execute("");
	    
	    return view;
	}


}