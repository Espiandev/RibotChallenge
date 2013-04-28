package com.espian.ribotchallenge.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.espian.ribotchallenge.R;

/**
 * Author: Alex Curran
 * Date: 28/04/2013
 */
public class SquareImageView extends ImageView {

	public enum PriorityDimension {
		width, height
	}

	private PriorityDimension priorityDimension;

	public SquareImageView(Context context) {
		super(context, null, 0);
	}

	public SquareImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView);
		String priorityString = array.getString(R.styleable.SquareImageView_priorityDimension);
		if (priorityString.equals("height")) priorityDimension = PriorityDimension.height;
		else if (priorityString.equals("width"))  priorityDimension = PriorityDimension.width;
		else priorityDimension = null;
		Log.d("SquareImageView", String.valueOf(priorityDimension));
		array.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int maxDimension;
		if (priorityDimension == null) maxDimension = Math.max(getMeasuredWidth(), getMeasuredHeight());
		else if (priorityDimension == PriorityDimension.width) maxDimension = getMeasuredWidth();
		else if (priorityDimension == PriorityDimension.height) maxDimension = getMeasuredHeight();
		else maxDimension = Math.max(getMeasuredWidth(), getMeasuredHeight());

		setMeasuredDimension(maxDimension, maxDimension);
	}
}
