package com.cmeiyuan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.cmeiyuan.widget.R;

public class ProgressView extends View {

	public static final int DRAW_FROM_START = 1;
	public static final int DRAW_FROM_CENTER = 2;
	public static final int DRAW_FROM_END = 3;
	private static final float CLIP_FACTOR = 0.05f;

	private int mPositiveDrawableResId;
	private int mNegativeDrawableResId;
	private Drawable mPositiveDrawable;
	private Drawable mNegativeDrawable;
	private Drawable mPositiveClipDrawable;
	private Drawable mNegativeClipDrawable;
	private float mProgressValue = 0.0f;
	private float mScaleFactor = 1.0f;
	private int mDrawType = DRAW_FROM_START;

	public ProgressView(Context context) {
		this(context, null);
	}

	public ProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		/** 可配属性设置 */
		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ProgressView, defStyleAttr, 0);
		mProgressValue = a.getFloat(R.styleable.ProgressView_progress, 0);
		mDrawType = a.getInt(R.styleable.ProgressView_drawType, 1);
		mPositiveDrawable = a
				.getDrawable(R.styleable.ProgressView_positiveDrawable);
		mNegativeDrawable = a
				.getDrawable(R.styleable.ProgressView_negativeDrawable);
		mPositiveDrawableResId = a.getResourceId(
				R.styleable.ProgressView_positiveDrawable, 0);
		mNegativeDrawableResId = a.getResourceId(
				R.styleable.ProgressView_negativeDrawable, 0);
		a.recycle();
		
		caculateScaleFactor();
	}

	private void caculateScaleFactor() {
		if (mDrawType == DRAW_FROM_CENTER) {
			mScaleFactor = 0.5f;
		} else {
			mScaleFactor = 1.0f;
		}
	}

	public void setDrawType(int type) {
		this.mDrawType = type;
	}

	public int getDrawType() {
		return mDrawType;
	}

	public void setProgress(float progress) {
		this.mProgressValue = progress;
		invalidate();
	}

	public float getProgress() {
		return mProgressValue;
	}

	public void setPositiveDrawable(Drawable drawable) {
		this.mPositiveDrawable = drawable;
	}

	public void setNegativeDrawable(Drawable drawable) {
		this.mNegativeDrawable = drawable;
	}

	public void setPositiveResource(int resId) {
		this.mPositiveDrawable = null;
		this.mPositiveDrawableResId = resId;
	}

	public void setNegativeResource(int resId) {
		this.mNegativeDrawable = null;
		this.mNegativeDrawableResId = resId;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		caculateScaleFactor();
		if (mProgressValue > 0) {
			drawPositiveProgress(canvas);
		} else if (mProgressValue < 0) {
			drawNegativeProgress(canvas);
		}
	}

	private Drawable getPositiveDrawable(float percent) {
		Drawable drawable = null;
		if (mPositiveDrawable == null && mPositiveDrawableResId != 0) {
			mPositiveDrawable = getContext().getResources().getDrawable(
					mPositiveDrawableResId);
		}
		drawable = mPositiveDrawable;

		if (mDrawType == DRAW_FROM_CENTER) {
			return drawable;
		}

		if (mPositiveDrawable != null && Math.abs(percent) <= CLIP_FACTOR) {
			if (mPositiveClipDrawable == null) {
				mPositiveClipDrawable = new ClipDrawable(mPositiveDrawable,
						Gravity.LEFT, ClipDrawable.HORIZONTAL);
			}
			drawable = mPositiveClipDrawable;
		}
		return drawable;
	}

	private Drawable getNegativeDrawable(float percent) {
		Drawable drawable = null;
		if (mNegativeDrawable == null && mNegativeDrawableResId != 0) {
			mNegativeDrawable = getContext().getResources().getDrawable(
					mNegativeDrawableResId);
		}
		drawable = mNegativeDrawable;

		if (mDrawType == DRAW_FROM_CENTER) {
			return drawable;
		}

		if (mNegativeDrawable != null && Math.abs(percent) <= CLIP_FACTOR) {
			if (mNegativeClipDrawable == null) {
				mNegativeClipDrawable = new ClipDrawable(mNegativeDrawable,
						Gravity.RIGHT, ClipDrawable.HORIZONTAL);
			}
			drawable = mNegativeClipDrawable;
		}
		return drawable;
	}

	private void drawPositiveProgress(Canvas canvas) {
		float percent = Math.abs(mProgressValue);
		Drawable drawable = getPositiveDrawable(percent);
		if (drawable != null) {
			int pl = getPaddingLeft();
			int pt = getPaddingTop();
			int pr = getPaddingRight();
			int pb = getPaddingBottom();
			int totalWidth = getWidth() - pl - pr;
			int totalHeight = getHeight() - pt - pb;
			int w = (int) (totalWidth * percent * mScaleFactor);
			int h = totalHeight;
			if (drawable instanceof ClipDrawable) {
				drawable.setBounds(0, 0, totalWidth, totalHeight);
				drawable.setLevel((int) (percent * 10000 * 0.9f));
			} else {
				drawable.setBounds(0, 0, w, h);
			}
			pl += (1 - mScaleFactor) * totalWidth;
			canvas.translate(pl, pt);
			drawable.draw(canvas);
			canvas.translate(-pl, -pt);
		}
	}

	private void drawNegativeProgress(Canvas canvas) {
		float percent = Math.abs(mProgressValue);
		Drawable drawable = getNegativeDrawable(percent);
		if (drawable != null) {
			int pl = getPaddingLeft();
			int pt = getPaddingTop();
			int pr = getPaddingRight();
			int pb = getPaddingBottom();
			int totalWidth = getWidth() - pl - pr;
			int totalHeight = getHeight() - pt - pb;
			int w = (int) (totalWidth * percent * mScaleFactor);
			int h = totalHeight;
			if (drawable instanceof ClipDrawable) {
				drawable.setBounds(0, 0, totalWidth, totalHeight);
				drawable.setLevel((int) (percent * 10000 * 0.9f));
				pl -= (1 - mScaleFactor) * totalWidth;
			} else {
				drawable.setBounds(0, 0, w, h);
				pl += totalWidth * mScaleFactor - w;
			}

			canvas.translate(pl, pt);
			drawable.draw(canvas);
			canvas.translate(-pl, -pt);
		}
	}

}
