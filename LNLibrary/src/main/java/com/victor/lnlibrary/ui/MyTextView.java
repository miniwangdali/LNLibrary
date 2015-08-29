package com.victor.lnlibrary.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.TextView;

public class MyTextView extends TextView {
	
	private String content;
	private SparseArray<Integer> startPos = new SparseArray<Integer>();
	private SparseArray<Integer> endPos = new SparseArray<Integer>();
	private int currentPage = 0;
	
	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	/*public MyTextView(Context context, String content) {
		super(context);
		// TODO Auto-generated constructor stub
		this.content = content;
		startPos.put(1, 0);
		setText(content);
	}*/
	
	public MyTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		//resize();
	}

	/**
     * 去除当前页无法显示的字
     * @return 去掉的字数
     */
    public int resize() {
        CharSequence oldContent = getText();
        currentPage ++;
        int end = getCharNum();
        startPos.put(currentPage, endPos.get(currentPage - 1, 0));
        endPos.put(currentPage, end);
        CharSequence newContent = oldContent.subSequence(0, end);
        setText(newContent);
        return oldContent.length() - newContent.length();
    }
    /**
     * 获取当前页总字数
     */
    public int getCharNum() {
        return getLayout().getLineEnd(getLineNum());
    }
 
    /**
     * 获取当前页总行数
     */
    public int getLineNum() {
        Layout layout = getLayout();
        
        int topOfLastLine = getHeight() - getPaddingTop() - getPaddingBottom() - getLineHeight();
        return layout.getLineForVertical(topOfLastLine);
    }

}
