package com.victor.lnlibrary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.config.StaticConfig;
import com.victor.lnlibrary.dao.BlockTask;
import com.victor.lnlibrary.ui.BlockLayout;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment{
	private List<String> blockList = null;
	private LinearLayout mLayout;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_home, container, false);
	    mLayout = ((LinearLayout)view.findViewById(R.id.homeLayout));
	    mLayout.addView(new BlockLayout(getActivity(), "我的书库"));
	    if (StaticConfig.hasInternet(getActivity())){
	    	if(blockList != null){
	    		for(String block : blockList){
	    			BlockLayout blockLayout = new BlockLayout(getActivity(), block);
	    			mLayout.addView(blockLayout);
	    		}
	    	}else{
	    		blockList = new ArrayList<String>();
	    		new BlockTask(getActivity(), this, mLayout).execute("");
	    	}
	     
	    }else{
	    	Toast.makeText(getActivity(), "无网络连接", Toast.LENGTH_SHORT).show();
	    }
	    return view;
	}

	public void setBlockList(List<String> blocks){
		if(blockList == null){
			blockList = new ArrayList<String>();
			blockList.addAll(blocks);
		}else{
			blockList.clear();
		    blockList.addAll(blocks);
		}
    
  }
}