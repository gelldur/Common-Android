package com.dexode.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dawid Drozd aka Gelldur on 9/15/15.
 */
public class InternetUtils {

	public static boolean isInternetConnectionAvailable(Context appContext) {
		ConnectivityManager connectivity =
				(ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
		if (networkInfo == null) {
			// There are no active networks.
			return false;
		} else {
			return networkInfo.isConnected();
		}
	}

	public static boolean isTrueInternetConnection(Context appContext) throws IOException {
		return isTrueInternetConnection(appContext, 1500);
	}

	public static boolean isTrueInternetConnection(Context appContext, int connectionTimeout) throws IOException {
		if (isInternetConnectionAvailable(appContext) == false) {
			return false;
		}

		HttpURLConnection urlConnection =
				(HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
		urlConnection.setRequestProperty("User-Agent", "Android");
		urlConnection.setRequestProperty("Connection", "close");
		urlConnection.setConnectTimeout(connectionTimeout);
		urlConnection.connect();
		return (urlConnection.getResponseCode() == 204 && urlConnection.getContentLength() == 0);
	}

	public static boolean isConnectedByWiFi(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		final NetworkInfo wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifi == null) {
			return false;
		}

		return wifi.isConnected();
	}

	public static boolean isConnectedByMobileData(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		final NetworkInfo mobile = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobile == null) {
			return false;
		}

		return mobile.isConnected();
	}
}
