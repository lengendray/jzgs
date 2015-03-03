package com.cmeiyuan.widget.interpolator;

import android.view.animation.Interpolator;

public class ZhuniInterpolator implements Interpolator {

	@Override
	public float getInterpolation(float arg0) {
		return (float) animationCurveVibrationDamping(arg0);
	}

	private double animationCurveVibrationDamping(float state) {
		return 1 - Math.exp(-3 * state) * Math.cos(state * 3.5 * Math.PI);
	}

}