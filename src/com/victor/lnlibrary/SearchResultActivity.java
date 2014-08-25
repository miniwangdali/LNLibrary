package com.victor.lnlibrary;

import com.example.lnlibrary.R;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class SearchResultActivity extends Activity{
	
	
	
	
  @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		doSearchQuery(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		doSearchQuery(intent);
	}

	private void doSearchQuery(Intent intent){
		if(intent == null){
			return;
		}
		String queryAciton = intent.getAction();
		if(queryAciton.equals(Intent.ACTION_SEARCH)){
			String queryString = intent.getStringExtra(SearchManager.QUERY);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment_BookList fragment_BookList = new Fragment_BookList();
			fragment_BookList.setTitle(queryString);
			fragment_BookList.setCommand("search");
			ft.add(R.id.fragment_home, fragment_BookList);
			ft.commit();
		}
	}
  
}