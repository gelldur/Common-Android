package com.squareup.okhttp.interceptor;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Dawid Drozd aka Gelldur on 9/18/15.
 * Interceptor that changes resposne form server
 */
public class NetworkInterceptor implements Interceptor {

	@Override
	public Response intercept(final Chain chain) throws IOException {
		Response originalResponse = chain.proceed(chain.request());
		return originalResponse.newBuilder()
							   .removeHeader("Pragma")
							   .header("Cache-Control", String.format("max-age=%d", 60))
							   .build();
	}
}
