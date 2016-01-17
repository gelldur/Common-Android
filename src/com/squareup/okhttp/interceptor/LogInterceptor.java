package com.squareup.okhttp.interceptor;

import com.dexode.util.log.Logger;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;

public class LogInterceptor implements Interceptor {

	private static final String F_BREAK = " %n";
	private static final String F_URL = " %s";
	private static final String F_TIME = " in %.1fms";
	private static final String F_HEADERS = "%s";
	private static final String F_RESPONSE = F_BREAK + "Response: %d";
	private static final String F_BODY = "body: %s";

	private static final String F_BREAKER = F_BREAK + "-------------------------------------------" + F_BREAK;
	private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS;
	private static final String F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BREAKER;
	private static final String F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK;
	private static final String F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER;

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();

		long t1 = System.nanoTime();
		Response response = chain.proceed(request);
		long t2 = System.nanoTime();

		MediaType contentType = null;
		String bodyString = null;
		if (response.body() != null) {
			contentType = response.body().contentType();
			bodyString = response.body().string();
		}

		double time = (t2 - t1) / 1e6d;

		Logger.i(
				"okhttp ##################################################################################################");

		if (request.method().equals("GET")) {
			Logger.i(String.format("okhttp GET " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY, request.url(), time,
								   request.headers(), response.code(), response.headers(), stringifyResponseBody(
							 bodyString)));
		} else if (request.method().equals("POST")) {
			Logger.i(String.format("okhttp POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time,
								   request.headers(), stringifyRequestBody(request), response.code(),
								   response.headers(), stringifyResponseBody(bodyString)));
		} else if (request.method().equals("PUT")) {
			Logger.i(String.format("okhttp PUT " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time,
								   request.headers(), request.body().toString(), response.code(), response.headers(),
								   stringifyResponseBody(bodyString)));
		} else if (request.method().equals("DELETE")) {
			Logger.i(String.format("okhttp DELETE " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY, request.url(),
								   time, request.headers(), response.code(), response.headers()));
		}

		if (response.body() != null) {
			ResponseBody body = ResponseBody.create(contentType, bodyString);
			return response.newBuilder().body(body).build();
		} else {
			return response;
		}
	}


	private static String stringifyRequestBody(Request request) {
		try {
			final Request copy = request.newBuilder().build();
			final Buffer buffer = new Buffer();
			copy.body().writeTo(buffer);
			return buffer.readUtf8();
		} catch (final IOException e) {
			return "IOException!";
		}
	}

	public String stringifyResponseBody(String responseBody) {
		return responseBody;
	}
}