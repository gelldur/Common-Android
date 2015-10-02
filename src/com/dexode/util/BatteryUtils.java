package com.dexode.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by Dawid Drozd aka Gelldur on 9/15/15.
 */
public class BatteryUtils {

	public static class BatteryStatus {

		public BatteryStatus(final float level, final boolean isCharging) {
			_level = level;
			_isCharging = isCharging;
		}

		public boolean isCharging() {
			return _isCharging;
		}

		public float getLevel() {
			return _level;
		}

		private float _level = 0;
		private boolean _isCharging = false;
	}

	public static BatteryStatus getBatteryStatus(final Context context) throws BatteryStatusException {
		Intent batteryIntent =
				context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		if (batteryIntent == null) {
			throw new BatteryStatusException();
		}
		final int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		final int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		final int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

		// Error checking that probably isn't needed but I added just in case.
		if (level == -1 || scale == -1 || status == -1) {
			throw new BatteryStatusException();
		}

		final boolean isCharging =
				status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

		return new BatteryStatus(((float) level / (float) scale) * 100.0f, isCharging);
	}

	public static class BatteryStatusException extends Exception {
	}
}
