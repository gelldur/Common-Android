package com.dexode.util;

/**
 * Created by Dawid Drozd aka Gelldur on 23.02.15.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dexode.util.log.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class DeviceId {

	public static String getDeviceId(Context appContext) throws RuntimeException {
		String deviceId = null;
		if (deviceId == null && checkPermission(appContext, "android.permission.ACCESS_WIFI_STATE")) {
			deviceId = getWiFiMacAdress(appContext);
			if (deviceId != null) {
				return deviceId;
			}
		} else {
			Logger.i("Please set android.permission.ACCESS_WIFI_STATE permission to get better user id");
		}

		if (deviceId == null && checkPermission(appContext, "android.permission.BLUETOOTH")) {
			deviceId = getBluetoothMacAdress(appContext);
			if (deviceId != null) {
				return deviceId;
			}
		} else {
			Logger.i("Please set android.permission.BLUETOOTH permission to get better user id");
		}

		deviceId = getAndroidId(appContext);

		// This shouldn't happen
		if (deviceId == null) {
			Logger.e("Can't get device ID!");
			throw new RuntimeException("Problems during getting ID");
		}

		return deviceId;
	}

	public static void backupFile(Context appContext, String folderName, String fileName, String deviceId) {
		if (checkPermission(appContext, "android.permission.WRITE_EXTERNAL_STORAGE") == false) {
			return;
		}
		String root = Environment.getExternalStorageDirectory().toString();
		File directory = new File(root + File.separator + folderName);
		if (directory.mkdirs() == false || directory.isDirectory() == false) {
			return;
		}

		FileOutputStream out = null;

		try {
			File file = new File(directory, fileName);
			if (file.exists() == false) {
				file.createNewFile();
			}

			out = new FileOutputStream(file);
			out.write(deviceId.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Nullable
	public static String readFromBuckup(Context appContext, String folderName, String fileName) {
		if (checkPermission(appContext, "android.permission.READ_EXTERNAL_STORAGE") == false) {
			return null;
		}
		String root = Environment.getExternalStorageDirectory().toString();
		File directory = new File(root + File.separator + folderName);
		if (directory.exists() == false || directory.isDirectory() == false) {
			return null;
		}

		BufferedReader reader = null;
		try {
			File file = new File(directory, fileName);
			if (file.exists() == false) {
				return null;
			}
			FileReader fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);

			final String deviceId = reader.readLine();
			return deviceId;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String generateRandomId() {
		return UUID.randomUUID().toString();
	}

	public static boolean checkPermission(Context appContext, String permission) {
		int res = appContext.checkCallingOrSelfPermission(permission);
		return (res == PackageManager.PERMISSION_GRANTED);
	}

	public static String getWiFiMacAdress(Context appContext) {
		WifiManager wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager == null) {
			return null;
		}

		WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		if (connectionInfo == null) {
			return null;
		}

		return connectionInfo.getMacAddress();
	}

	public static String getBluetoothMacAdress(Context appContext) {
		BluetoothAdapter adapter = null;
		try {
			adapter = BluetoothAdapter.getDefaultAdapter();
		} catch (RuntimeException ex) {
			//Probably error:
			//Can't create handler inside thread that has not called Looper.prepare()
			throw new RuntimeException(
					"Please call getDefaultAdapter once in main thread http://stackoverflow.com/questions/5920578");
		} catch (Exception ex) {
			return null;
		}
		if (adapter != null) {
			return adapter.getAddress();
		} else {
			Log.w(DeviceId.class.getSimpleName(), "No bluetooth adapter");
			return null;
		}
	}

	public static String getAndroidId(Context appContext) {
		return Secure.getString(appContext.getContentResolver(), Secure.ANDROID_ID);
	}

	public static String getAccountId(Context appContext) {
		if (checkPermission(appContext, "android.permission.GET_ACCOUNTS") == false) {
			Log.w(DeviceId.class.getSimpleName(),
				  "Please set android.permission.GET_ACCOUNTS permission to get accountId");
			return null;
		}

		try {
			final Account[] accounts = AccountManager.get(appContext).getAccounts();
			for (Account account : accounts) {
				if (account.name.contains("@")) {
					return account.name;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
