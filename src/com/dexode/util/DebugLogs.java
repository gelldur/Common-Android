package com.dexode.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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

	public DebugLogs(boolean isDebugable, String outputFileName) {
		_isDebug = isDebugable;
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

			_fileOutputStream.write(_stringBuilder.toString().getBytes());
			appendNewLine();

			_fileOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void printStackTrace(Throwable throwable) {
		if (_isDebug == false) {
			return;
		}
		PrintStream printStream = new PrintStream(_fileOutputStream);
		throwable.printStackTrace(printStream);
	}

	private void appendTime() throws IOException {
		if (_isDebug == false) {
			return;
		}
		String fullDate = TimeFormatter.getFullDate(System.currentTimeMillis());
		fullDate += ":\t";
		_fileOutputStream.write(fullDate.getBytes());
	}

	private void appendNewLine() throws IOException {
		if (_isDebug == false) {
			return;
		}
		_fileOutputStream.write("\n".getBytes());
	}

	public final boolean _isDebug;
	private static DebugLogs _instance;
	private StringBuilder _stringBuilder = new StringBuilder(1024);
	private FileOutputStream _fileOutputStream;
	private File _outputFile;
}
