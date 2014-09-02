package com.victor.lnlibrary;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.bean.Config;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

public class SettingsActivity extends PreferenceActivity {

	SharedPreferences settings = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.settings);
		
		settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
		
		
		//字号
		Preference fontSize = (Preference)findPreference("fontsize");
		fontSize.setSummary("当前字体大小：" + Config.getFontsize());
		fontSize.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				
				
				
				return true;
			}
		});
		//行间距
		Preference linespace = (Preference)findPreference("linespace");
		linespace.setSummary("当前行间距：" + Config.getLinespace());
		linespace.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				
				return true;
			}
		});
		
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
		
		//关于
		Preference about = (Preference)findPreference("about");
		linespace.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				
				return true;
			}
		});
	}
	
	

	
}
