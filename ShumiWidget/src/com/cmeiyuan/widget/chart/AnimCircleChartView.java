package com.cmeiyuan.widget.chart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.cmeiyuan.widget.chart.CircleChartView;
import com.nineoldandroids.animation.ObjectAnimator;

public class AnimCircleChartView extends CircleChartView {

	private long mDuration = 500;
	private ObjectAnimator mObjectAnimator;
	private Interpolator mInterpolator = new LinearInterpolator();
	
	

	public AnimCircleChartView(Context context) {
		super(context);
		init();
	}

	public AnimCircleChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AnimCircleChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mObjectAnimator = new ObjectAnimator();
		mObjectAnimator.setTarget(this);
		mObjectAnimator.setPropertyName("percent");
		mObjectAnimator.setInterpolator(mInterpolator);
		mObjectAnimator.setDuration(mDuration);
	}

	public void setInterpolator(Interpolator value) {
		mObjectAnimator.setInterpolator(value);
	}

	public void setDuration(long duration) {
		this.mDuration = duration;
	}

	public long getDuration() {
		return mDuration;
	}

	public void startAnimation() {
		stopAnimation();
		int duratoin = (int) mDuration;
		mObjectAnimator.setDuration(Math.max(100, duratoin));
		mObjectAnimator.setFloatValues(0, getPercent());
		mObjectAnimator.start();
	}
	
	@Deprecated
	public void startAnimation(long delay) {
		startAnimation();
	}

	public void stopAnimation() {
		if (mObjectAnimator.isRunning()) {
			mObjectAnimator.end();
		}
	}

}
