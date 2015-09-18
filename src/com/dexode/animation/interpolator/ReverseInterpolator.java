package com.dexode.animation.interpolator;

import android.view.animation.Interpolator;

public class ReverseInterpolator implements Interpolator {

	private Interpolator _interpolator;

	public ReverseInterpolator(Interpolator interpolator) {
		_interpolator = interpolator;
	}

	@Override
	public float getInterpolation(float paramFloat) {
		return Math.abs(_interpolator.getInterpolation(paramFloat) - 1f);
	}
}