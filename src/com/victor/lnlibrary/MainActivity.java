package com.victor.lnlibrary;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

import android.widget.SearchView;
import android.widget.Toast;

import com.example.lnlibrary.R;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.fragment_home, new Fragment_Home());
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.main, menu);
		SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
		if (searchView == null){
			Toast.makeText(this, "找不到搜索框", Toast.LENGTH_SHORT).show();
			return true;
		}
		searchView.setIconifiedByDefault(true);
		SearchableInfo searchableInfo = ((SearchManager)getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(new ComponentName(this, SearchResultActivity.class));
		if (searchableInfo == null){
			Toast.makeText(this, "info is null", Toast.LENGTH_SHORT).show();
		}
		searchView.setSearchableInfo(searchableInfo);
		return true;
		
	}
}