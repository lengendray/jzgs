package com.cmeiyuan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.ObjectAnimator;

public class AnimProgressView extends ProgressView {

	private long mDuration = 500;
	private ObjectAnimator mObjectAnimator;

	public AnimProgressView(Context context) {
		super(context);
		init();
	}

	public AnimProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AnimProgressView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mObjectAnimator = new ObjectAnimator();
		mObjectAnimator.setTarget(this);
		mObjectAnimator.setPropertyName("progress");
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
		int duration = (int) (mDuration * Math.abs(getProgress()));
		mObjectAnimator.setDuration(Math.max(100, duration));
		mObjectAnimator.setFloatValues(0, getProgress());
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
