package com.dexode.util;

import android.content.Context;

/**
 * Created by Dawid Drozd aka Gelldur on 9/15/15.
 */
public class ApplicationContext {
	public static void set(Context context) {
		_applicationContext = context.getApplicationContext();
	}

	public static Context get() {
		return _applicationContext;
	}

	private static Context _applicationContext;
}
