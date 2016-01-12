package com.dexode.util;

import android.support.annotation.Nullable;

/**
 * Created by Dawid Drozd aka Gelldur on 1/12/16.
 */
public class Assert {
	public static void check(boolean value) {
		check(value, null);
	}

	public static void check(boolean value, @Nullable String message) {
		if (value) {
			return;
		}
		throw new AssertionError(message);
	}
}
