package com.victor.lnlibrary;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.LinearLayout.LayoutParams;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.dao.FuncTask;

public class Fragment_BookList extends Fragment{
	private String command = new String();
	private LinearLayout mLayout = null;
	private String title = new String();
	private View view = null;
	
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
		if(view == null){
			view = inflater.inflate(R.layout.fragment_booklist, container, false);
		    mLayout = (LinearLayout)view.findViewById(R.id.booklistlayout);
		    /*ProgressBar progressBar = new ProgressBar(getActivity());
		    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		    progressBar.setLayoutParams(params);
			progressBar.setIndeterminate(true);
			mLayout.addView(progressBar, 0);*/
		    FuncTask mFuncTask = new FuncTask(getActivity(), mLayout, command, title);
		    mFuncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
		    return view;
		}else{
			return view;
		}
		
	}


}