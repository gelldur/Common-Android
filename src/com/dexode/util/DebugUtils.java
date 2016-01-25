package com.dexode.util;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by Dawid Drozd aka Gelldur on 9/15/15.
 * This class shouldn't work in release mode. You should strip this code by proguard :)
 * This code should be used only for debug purposes.
 */
public class DebugUtils {
	public static String streamToString(final InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			builder.append(line);
		}
		return builder.toString();
	}

	public static String bundle2string(Bundle bundle) {
		if (bundle == null) {
			return "null";
		}
		String string = "Bundle{";
		for (String key : bundle.keySet()) {
			string += " " + key + " => " + bundle.get(key) + ";";
		}
		string += " }Bundle";
		return string;
	}

	public static String toStringRequest(Request request) {
		StringBuilder stringBuilder = new StringBuilder(1024);

		stringBuilder.append("curl -X ").append(request.method()).append(" ");
		final Headers headers = request.headers();
		for (String name : headers.names()) {
			stringBuilder.append("--header \"").append(name).append(":").append(headers.get(name)).append("\" ");
		}

		try {
			if (request.body() != null && request.body().contentLength() > 1) {
				Buffer buffer = new Buffer();

				request.body().writeTo(buffer);
				stringBuilder.append("-d \"");

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(buffer.inputStream()));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line).append("\\n");
				}
				stringBuilder.append("\"");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		stringBuilder.append(" \"").append(request.url().toString()).append("\"");

		return stringBuilder.toString();
	}

	public static String toStringResponse(Response response) {
		StringBuilder stringBuilder = new StringBuilder(1024);

		stringBuilder.append(response.toString());
		final Headers headers = response.headers();
		for (String name : headers.names()) {
			stringBuilder.append(name).append(":").append(headers.get(name)).append("\n");
		}

		if (response.body() != null) {
			try {
				stringBuilder.append(response.body().string());
			} catch (IOException e) {
				stringBuilder.append("exception during body read");
				e.printStackTrace();
			}
		}

		return stringBuilder.toString();
	}
}
