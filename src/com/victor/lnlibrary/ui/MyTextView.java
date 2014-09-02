package com.victor.lnlibrary.ui;

import android.content.Context;
import android.widget.TextView;

public class MyTextView extends TextView {

	public MyTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		String newText = new String(); 
		String[] tempString = text.toString().split("\n");
		for(int i = 0; i < tempString.length; i ++){
			tempString[i] = "\b\b\b\b\b\b\b" + tempString[i];
			newText = newText + tempString[i] + "\n";
		}
		super.setText(newText, type);
	}

	
}
