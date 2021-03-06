package com.victor.lnlibrary.ui;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;

public class Expand_Custom_Animation extends Animation 
{
	private View mAnimatedView;
	private LayoutParams mViewLayoutParams;
	private int mMarginStart, mMarginEnd;
	private boolean mIsVisibleAfter = false;
	private boolean mWasEndedAlready = false;

	public Expand_Custom_Animation(View view, int duration) 
	{
		setDuration(duration);
		mAnimatedView = view;
		mViewLayoutParams = (LayoutParams) view.getLayoutParams();
		mIsVisibleAfter = (view.getVisibility() == View.VISIBLE);
		mMarginStart = mViewLayoutParams.bottomMargin;
		mMarginEnd = (mMarginStart == 0 ? (0 - view.getHeight()) : 0);
		view.setVisibility(View.VISIBLE);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) 
	{
		super.applyTransformation(interpolatedTime, t);
		
		if (interpolatedTime < 1.0f) 
		{
			mViewLayoutParams.bottomMargin = mMarginStart + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);
			mAnimatedView.requestLayout();
		} 
		else if (!mWasEndedAlready)
		{
			mViewLayoutParams.bottomMargin = mMarginEnd;
			mAnimatedView.requestLayout();

			if (mIsVisibleAfter) 
			{
				mAnimatedView.setVisibility(View.GONE);
			}
			mWasEndedAlready = true;
		}
	}
}
