package com.victor.lnlibrary.dao;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.victor.lnlibrary.Fragment_Home;
import com.victor.lnlibrary.htmlparser.Blocks;
import com.victor.lnlibrary.ui.BlockLayout;
import java.util.ArrayList;
import java.util.List;

public class BlockTask extends AsyncTask<String, Integer, String>{

	private Activity mActivity;
	private Fragment_Home mFragment;
	private LinearLayout mLayout;
	private List<String> mList = new ArrayList<String>();

	public BlockTask(Activity activity, Fragment_Home fragment, LinearLayout layout){
		mFragment = fragment;
		mActivity = activity;
		mLayout = layout;
	}


	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try{
			mList = new Blocks().getBlockList();
			return "completed";
	    }catch (Exception e) {
			// TODO: handle exception
	    	e.printStackTrace();
	    	return e.toString();
		}
		
	}


  
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
	  	if(result.equals("completed")){
	  		for(String blockname : mList){
	  			BlockLayout blockLayout = new BlockLayout(mActivity, blockname);
	  			mLayout.addView(blockLayout);
	  		}
	  		mFragment.setBlockList(mList);
	  	}else{
	  		Toast.makeText(mActivity, "error", Toast.LENGTH_SHORT).show();
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
		super.onProgressUpdate(values);
	}

}