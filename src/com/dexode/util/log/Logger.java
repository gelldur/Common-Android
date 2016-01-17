package com.dexode.util.log;

import android.support.annotation.Nullable;

/**
 * Created by Dawid Drozd aka Gelldur on 9/18/15.
 */
public class Logger {

	/**
	 * This method should be removed in release code by proguard
	 */
	public static void d(final String text, Object... args) {
		if (_log != null) {
			_log.d(text, args);
		}
	}

	/**
	 * This method should be removed in release code by proguard
	 */
	public static void i(final String text, Object... args) {
		if (_log != null) {
			_log.i(text, args);
		}
	}

	/**
	 * This method should be removed in release code by proguard
	 */
	public static void w(final String text, Object... args) {
		if (_log != null) {
			_log.w(text, args);
		}
	}

	public static void e(final String text, Object... args) {
		if (_log != null) {
			_log.e(text, args);
		}
	}

	public static void e(final Exception exception, @Nullable final String text) {
		if (_log != null) {
			_log.e(exception, text);
		}
	}

	public static void e(final Exception exception) {
		if (_log != null) {
			_log.e(exception, null);
		}
	}


	public static void setLog(Log log) {
		_log = log;
	}

	@Nullable
	private static Log _log;

	public interface Log {
		/**
		 * This method should be removed in release code by proguard
		 */
		public void d(final String text, Object... args);

		/**
		 * This method should be removed in release code by proguard
		 */
		public void i(final String text, Object... args);

		/**
		 * This method should be removed in release code by proguard
		 */
		public void w(final String text, Object... args);

		public void e(final String text, Object... args);

		public void e(final Exception exception, @Nullable final String text);
	}
}
