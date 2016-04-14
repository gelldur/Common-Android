package com.dexode.util;

/**
 * Created by Dawid Drozd aka Gelldur on 23.02.15.
 */

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.dexode.util.log.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DeviceId {

	@NonNull
	public static String getDeviceId(Context appContext) throws RuntimeException {
		final String deviceId = getDeviceIdBestFit(appContext);
		return deviceId.toLowerCase();
	}

	@NonNull
	private static String getDeviceIdBestFit(Context appContext) throws RuntimeException {
		String deviceId = null;
		if (deviceId == null && checkPermission(appContext, Manifest.permission.ACCESS_WIFI_STATE)) {
			deviceId = getWiFiMacAddress(appContext);
			if (deviceId != null) {
				return deviceId;
			}
		} else {
			Logger.i("Please set android.permission.ACCESS_WIFI_STATE permission to get better user id");
		}

		if (deviceId == null && checkPermission(appContext, Manifest.permission.BLUETOOTH)) {
			deviceId = getBluetoothMacAddress(appContext);
			if (deviceId != null) {
				return deviceId;
			}
		} else {
			Logger.i("Please set android.permission.BLUETOOTH permission to get better user id");
		}

		deviceId = getAndroidId(appContext);

		// This shouldn't happen
		if (deviceId == null) {
			Logger.e("Can't get device ID! Using random ID");
			return generateRandomId();
		}

		return deviceId;
	}

	public static void backupFile(Context appContext, String folderName, String fileName, String deviceId) {
		if (checkPermission(appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == false) {
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
				file.setReadable(true, false);
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
		if (checkPermission(appContext, Manifest.permission.READ_EXTERNAL_STORAGE) == false) {
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
		return ContextCompat.checkSelfPermission(appContext, permission) == PackageManager.PERMISSION_GRANTED;
	}

	@Nullable
	public static String getWiFiMacAddress(Context appContext) {
		WifiManager wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager == null) {
			return null;
		}

		WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		if (connectionInfo == null) {
			return null;
		}

		String macAddress = connectionInfo.getMacAddress();
		//Check for mocked value
		if (isMockedAddress(macAddress)) {
			macAddress = null;
		}

		if (macAddress == null && checkPermission(appContext, Manifest.permission.INTERNET)) {
			try {
				macAddress = getWiFiMacAddressHack_1();
				if (isMockedAddress(macAddress)) {
					macAddress = null;
				}
			} catch (SocketException e) {
				Logger.e(e);
			}
		}

		return macAddress;
	}

	@Nullable
	private static String getWiFiMacAddressHack_1() throws SocketException {
		List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
		for (NetworkInterface networkInterface : networkInterfaces) {
			if (networkInterface.getName().equalsIgnoreCase("wlan0") == false) {
				continue;
			}

			byte[] macBytes = networkInterface.getHardwareAddress();
			if (macBytes == null) {
				return null;
			}

			StringBuilder builder = new StringBuilder();
			for (byte oneByte : macBytes) {
				builder.append(String.format("%02X:", (oneByte & 0xFF)));
			}

			if (builder.length() > 0) {
				builder.deleteCharAt(builder.length() - 1);
			}
			return builder.toString();
		}
		return null;
	}

	@SuppressWarnings("MissingPermission")
	@Nullable
	public static String getBluetoothMacAddress(Context appContext) {
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
			final String macAddress = adapter.getAddress();
			//Check for mocked value
			if (isMockedAddress(macAddress)) {
				return null;
			}
			return macAddress;
		} else {
			Log.w(DeviceId.class.getSimpleName(), "No bluetooth adapter");
			return null;
		}
	}

	public static String getAndroidId(Context appContext) {
		return Secure.getString(appContext.getContentResolver(), Secure.ANDROID_ID);
	}

	@SuppressWarnings("MissingPermission")
	public static String getAccountId(Context appContext) {
		if (checkPermission(appContext, Manifest.permission.GET_ACCOUNTS) == false) {
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

	private static boolean isMockedAddress(@Nullable final String macAddress) {
		return macAddress == null || macAddress.equals("02:00:00:00:00:00");
	}
}
