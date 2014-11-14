package com.victor.lnlibrary;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.SearchView;
import android.widget.Toast;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.config.Config;
import com.victor.lnlibrary.dao.UpdateTask;

public class MainActivity extends Activity{

	Activity mActivity = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbarlayer));
		actionBar.setIcon(R.drawable.icon);
		
		
		setContentView(R.layout.activity_main);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.fragment_home, new Fragment_Home());
		ft.commit();
		
		SharedPreferences settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
		Config.setFontsize(settings.getFloat("fontsize", 18.0f));
		Config.setLinespace(settings.getFloat("linespace", 1.2f));
		Config.setNightmode(settings.getBoolean("nightmode", false));
		Config.setAwake(settings.getBoolean("awake", false));
		
		UpdateTask mUpdateTask = new UpdateTask(this);
		mUpdateTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_setting:
			
			Intent intent = new Intent();
			intent.setClass(this, SettingsActivity.class);
			startActivity(intent);
			
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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