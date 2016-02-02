package com.dexode.util;

import com.dexode.util.log.Logger;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by Dawid Drozd aka Gelldur on 11.03.15.
 */
public class UtilsHash {

	public static String md5(String input) {
		MessageDigest digest;
		try {
			byte[] bytes = input.getBytes("UTF-8");

			digest = MessageDigest.getInstance("MD5");
			digest.update(bytes);
			return new BigInteger(1, digest.digest()).toString(16);
		} catch (Exception e) {
			Logger.e(e);
		}
		return "no_hash";
	}

	public static String sha1(String input) {
		MessageDigest digest;
		try {
			byte[] bytes = input.getBytes("UTF-8");

			digest = MessageDigest.getInstance("SHA1");
			digest.update(bytes);
			return new BigInteger(1, digest.digest()).toString(16);
		} catch (Exception e) {
			Logger.e(e);
		}
		return "no_hash";
	}

	public static boolean compareMd5(String left, String right) {
		if (left == null || right == null) {
			//Yes compre references. If we have 2x null then ok else it will return false
			return left == right;
		}

		BigInteger bigIntegerLeft = new BigInteger(left, 16);
		BigInteger bigIntegerRight = new BigInteger(right, 16);
		return bigIntegerLeft.equals(bigIntegerRight);
	}

}
