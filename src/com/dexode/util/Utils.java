package com.dexode.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Dawid Drozd aka Gelldur on 9/15/15.
 */
public class Utils {
	/**
	 * This method converts dp unit to equivalent pixels, depending on device density.
	 *
	 * @param dp
	 * 		A value in dp (density independent pixels) unit. Which we need to convert into
	 * 		pixels
	 *
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(Context appContext, float dp) {
		Resources resources = appContext.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return (float) Math.ceil(dp * (metrics.densityDpi / 160f));
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 *
	 * @param px
	 * 		A value in px (pixels) unit. Which we need to convert into db
	 *
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(Context appContext, float px) {
		Resources resources = appContext.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static Point getScreenSize(Context appContext) {
		WindowManager windowManager = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		if (Build.VERSION.SDK_INT >= 13) {
			display.getSize(size);
		} else {
			size.x = display.getWidth();
			size.y = display.getHeight();
		}
		return size;
	}

	public static String getAppVersionName(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
}
