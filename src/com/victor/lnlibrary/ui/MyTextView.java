package com.victor.lnlibrary.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {
	
	
	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public MyTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/*@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		String result = new String();
		char[] textCharArray = text.toString().toCharArray();
		for(int i = 0; i < textCharArray.length; i ++){
			if(textCharArray[i] > 127 && textCharArray[i] != '、' && textCharArray[i] != '，' && textCharArray[i] != '。' && textCharArray[i] != '：' && textCharArray[i] != '！'){
				result = result + textCharArray[i];
			}else{
				result = result + textCharArray[i] + "\b"; 
			}
		}
		
		
		super.setText(result, type);
	}*/
	

	
}
