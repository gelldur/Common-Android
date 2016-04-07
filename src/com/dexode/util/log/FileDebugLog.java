package com.dexode.util.log;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Formatter;

/**
 * Created by Dawid Drozd aka Gelldur on 9/15/15.
 * This class shouldn't work in release mode. You should remove this code by proguard :)
 * This code should be used only for debug purposes.
 */
public class FileDebugLog implements Logger.Log {

	public FileDebugLog(boolean isQuiet, @Nullable String outputFileName) throws IOException {
		_isQuiet = isQuiet;
		if (outputFileName != null) {
			_outputFile = new File(Environment.getExternalStorageDirectory(), outputFileName);
			if (_outputFile.exists() == false) {
				_outputFile.createNewFile();
			}
			_outputFile.setReadable(true,false);
			_fileOutputStream = new FileOutputStream(_outputFile, true);
		}
	}


	/**
	 * {@inheritDoc}
	 * <p/>
	 * Use %d for int <br>
	 * Use %f for float/double <br>
	 * Use %b for boolean <br>
	 *
	 * @param text
	 * @param args
	 */
	@Override
	public void i(final String text, final Object... args) {
		d(text, args);
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Use %d for int <br>
	 * Use %f for float/double <br>
	 * Use %b for boolean <br>
	 *
	 * @param text
	 * @param args
	 */
	@Override
	public void w(final String text, final Object... args) {
		d(text, args);
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Use %d for int <br>
	 * Use %f for float/double <br>
	 * Use %b for boolean <br>
	 *
	 * @param text
	 * @param args
	 */
	@Override
	public void e(final String text, final Object... args) {
		d(text, args);
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 *
	 * @param exception
	 * @param text
	 */
	@Override
	public void e(final Exception exception, @Nullable final String text) {
		if (text != null) {
			d(text);
		}
		printStackTrace(exception);
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Use %d for int <br>
	 * Use %f for float/double <br>
	 * Use %b for boolean <br>
	 *
	 * @param text
	 * @param args
	 */
	@Override
	public void d(final String text, final Object... args) {
		try {
			_stringBuilder.setLength(0);
			if (args == null || args.length < 1) {
				_stringBuilder.append(text);
			} else {
				new Formatter(_stringBuilder).format(text, args);
			}

			byte[] bytes;
			{
				String string = _stringBuilder.toString();
				_stringBuilder.setLength(0);
				if (_isQuiet == false) {
					Log.d("DEBUG", string);
				}
				bytes = string.getBytes();
				string = null;
			}

			if (_fileOutputStream != null) {
				appendTime();
				final int maxChars = 2048;
				if (bytes.length > maxChars) {
					for (int i = 0; i < bytes.length; i += maxChars) {
						_fileOutputStream.write(bytes, i,
												(i + maxChars) > bytes.length ? (bytes.length - i) : maxChars);
						_fileOutputStream.write(10);//new line
					}
				} else {
					_fileOutputStream.write(bytes);
				}
				_fileOutputStream.write(10);//new line

				_fileOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void printStackTrace(Throwable throwable) {
		if (_fileOutputStream != null) {
			PrintStream printStream = new PrintStream(_fileOutputStream);
			throwable.printStackTrace(printStream);
		}
		throwable.printStackTrace();
	}

	private void appendTime() throws IOException {
		_date.setTime(System.currentTimeMillis());
		String fullDate = _fullTime.format(_date);
		fullDate += ":\t";
		assert _fileOutputStream != null;
		_fileOutputStream.write(fullDate.getBytes());
	}

	public boolean isQuiet() {
		return _isQuiet;
	}

	private final boolean _isQuiet;
	private StringBuilder _stringBuilder = new StringBuilder(1024);
	@Nullable
	private FileOutputStream _fileOutputStream;
	@Nullable
	private File _outputFile;
	private Date _date = new Date();
	java.text.SimpleDateFormat _fullTime = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");
}
