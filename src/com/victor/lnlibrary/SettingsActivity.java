package com.victor.lnlibrary;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.config.Config;
import com.victor.lnlibrary.ui.SeekPreference;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {

	SharedPreferences settings = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbarlayer));
		actionBar.setIcon(R.drawable.icon);
		
		addPreferencesFromResource(R.xml.settings);
		
		settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
		
		
		//字号
		SeekPreference fontSize = (SeekPreference)findPreference("fontsize");
		fontSize.setSummary(String.valueOf(Config.getFontsize()));
		
		//行间距
		SeekPreference linespace = (SeekPreference)findPreference("linespace");
		linespace.setSummary(String.valueOf(Config.getLinespace()));
		fontSize.notifyDependencyChange(false);
		
		//夜间模式
		CheckBoxPreference nightmode = (CheckBoxPreference)findPreference("nightmode");
		nightmode.setChecked(Config.isNightmode());
		nightmode.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				Config.setNightmode(!Config.isNightmode());
				Editor editor = settings.edit();
				editor.putBoolean("nightmode", Config.isNightmode());
				editor.commit();
				return true;
			}
		});
		
		//屏幕常亮
		CheckBoxPreference awake = (CheckBoxPreference)findPreference("awake");
		awake.setChecked(Config.isAwake());
		awake.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				Config.setAwake(!Config.isAwake());
				Editor editor = settings.edit();
				editor.putBoolean("awake", Config.isAwake());
				editor.commit();
				return true;
			}
		});		
		
		//检查更新
		Preference checkupdate = (Preference)findPreference("checkupdate");
		try {
			checkupdate.setSummary("当前版本：" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			checkupdate.setSummary("unknown");
		}
		checkupdate.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				
				return false;
			}
		});
		
		//关于
		Preference about = (Preference)findPreference("about");
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Editor editor = settings.edit();
		editor.putBoolean("nightmode", Config.isNightmode());
		editor.putFloat("fontsize", Config.getFontsize());
		editor.putFloat("linespace", Config.getLinespace());
		editor.putBoolean("awake", Config.isAwake());
		editor.commit();
		super.onStop();
	}
	
	

	
}
