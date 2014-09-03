package com.victor.lnlibrary.ui;

import com.example.lnlibrary.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		EditText sizeEditText = (EditText)view.findViewById(R.id.value);
		sizeEditText.setText(value);
		
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		return LayoutInflater.from(getContext()).inflate(R.layout.seekpreference, parent, false);
	}

}
