package com.victor.lnlibrary.ui;

import java.math.BigDecimal;

import com.example.lnlibrary.R;
import com.victor.lnlibrary.config.Config;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SeekPreference extends Preference {

	private String title, value;
	
	public SeekPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SeekPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SeekPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		title = getTitle().toString();
		value = getSummary().toString();
		super.onBindView(view);
		TextView titleTextView = (TextView)view.findViewById(R.id.pref_title);
		titleTextView.setText(title);
		final EditText sizeEditText = (EditText)view.findViewById(R.id.value);
		sizeEditText.setText(value);
		Button sizeup = (Button)view.findViewById(R.id.plus);
		sizeup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(title.equals("行间距")){
					float i = Float.parseFloat(value);
					i = i + 0.1f;
					BigDecimal bigDecimal = new BigDecimal((double)i);
					i = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
					value = String.valueOf(i);
					sizeEditText.setText(value);
					Config.setLinespace(i);
				}else if (title.equals("字体大小")) {
					float i = Float.parseFloat(value);
					i = i + 1;
					value = String.valueOf(i);
					sizeEditText.setText(value);
					Config.setFontsize(i);
				}
				
			}
		});
		Button sizedown = (Button)view.findViewById(R.id.decline);
		sizedown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(title.equals("行间距")){
					float i = Float.parseFloat(value);
					i = i - 0.1f;
					BigDecimal bigDecimal = new BigDecimal((double)i);
					i = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
					value = String.valueOf(i);
					sizeEditText.setText(value);
					Config.setLinespace(i);
				}else if (title.equals("字体大小")) {
					float i = Float.parseFloat(value);
					i = i - 1;
					value = String.valueOf(i);
					sizeEditText.setText(value);
					Config.setFontsize(i);
				}
			}
		});
		
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		return LayoutInflater.from(getContext()).inflate(R.layout.seekpreference, parent, false);
	}

}
