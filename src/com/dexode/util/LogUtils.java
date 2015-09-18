package com.dexode.util;

import android.support.annotation.Nullable;

/**
 * Created by Dawid Drozd aka Gelldur on 9/18/15.
 */
public class LogUtils {

	public static void log(final String text) {
		if (_log == null) {
			return;
		}
		_log.log(text);
	}

	public static void setLog(Log log) {
		_log = log;
	}

	@Nullable
	private static Log _log;

	public interface Log {
		public void log(String text);
	}
}
