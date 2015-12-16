package com.dexode.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Formatter;

/**
 * Created by Dawid Drozd aka Gelldur on 9/15/15.
 * This class shouldn't work in release mode. You should strip this code by proguard :)
 * This code should be used only for debug purposes.
 */
public class DebugLogs {

	public synchronized static void setInstance(DebugLogs debugLogs) {
		_instance = debugLogs;
	}

	public synchronized static DebugLogs getInstance() {
		return _instance;
	}

	public DebugLogs(boolean isDebug, String outputFileName) {
		_isDebug = isDebug;
		if (_isDebug == false) {
			return;
		}

		try {
			_outputFile = new File(Environment.getExternalStorageDirectory(), outputFileName);
			if (_outputFile.exists() == false) {
				_outputFile.createNewFile();
			}
			_fileOutputStream = new FileOutputStream(_outputFile, true);
		} catch (Exception ex) {
			throw new RuntimeException("I don't care!");
		}
	}

	/**
	 * Use %d for int
	 * Use %f for float/double
	 * Use %b for boolean
	 *
	 * @param message
	 * @param args
	 */
	public synchronized static void l(String message, Object... args) {
		if (_instance == null) {
			return;
		}
		if (_instance._isDebug == false) {
			return;
		}
		_instance.log(message, args);
	}

	/**
	 * Use %d for int
	 * Use %f for float/double
	 * Use %b for boolean
	 *
	 * @param message
	 * @param args
	 */
	public synchronized void log(String message, Object... args) {
		if (_isDebug == false) {
			return;
		}
		try {
			appendTime();
			_stringBuilder.setLength(0);
			if (args == null || args.length < 1) {
				_stringBuilder.append(message);
			} else {
				new Formatter(_stringBuilder).format(message, args);
			}

			byte[] bytes;
			{
				String string = _stringBuilder.toString();
				_stringBuilder.setLength(0);
				Log.d("DEBUG", string);
				bytes = string.getBytes();
				string = null;
			}

			final int maxChars = 2048;
			if (bytes.length > maxChars) {
				for (int i = 0; i < bytes.length; i += maxChars) {
					_fileOutputStream.write(bytes, i, (i + maxChars) > bytes.length ? (bytes.length - i) : maxChars);
					_fileOutputStream.write(10);//new line
				}
			} else {
				_fileOutputStream.write(bytes);
			}
			_fileOutputStream.write(10);//new line

			_fileOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logException(Throwable throwable) {
		if (_instance == null) {
			return;
		}
		if (_instance._isDebug == false) {
			return;
		}
		_instance.printStackTrace(throwable);
	}

	public synchronized void printStackTrace(Throwable throwable) {
		if (_isDebug == false) {
			return;
		}
		PrintStream printStream = new PrintStream(_fileOutputStream);
		throwable.printStackTrace(printStream);
		throwable.printStackTrace();
	}

	private void appendTime() throws IOException {
		if (_isDebug == false) {
			return;
		}
		_date.setTime(System.currentTimeMillis());
		String fullDate = _fullTime.format(_date);
		fullDate += ":\t";
		_fileOutputStream.write(fullDate.getBytes());
	}

	private final boolean _isDebug;
	private static DebugLogs _instance;
	private StringBuilder _stringBuilder = new StringBuilder(1024);
	private FileOutputStream _fileOutputStream;
	private File _outputFile;
	private Date _date = new Date();
	java.text.SimpleDateFormat _fullTime = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");
}
