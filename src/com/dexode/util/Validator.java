package com.dexode.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
 */
public class Validator {
	/**
	 * @param email
	 * 		email address for verification
	 *
	 * @return true if e-mail is acceptable, false otherwise
	 */
	public boolean validateEmail(String email) {
		Matcher matcher = PATTERN_EMAIL.matcher(email);
		return matcher.matches();
	}

	/**
	 * @param password
	 * 		password for verification
	 *
	 * @return 0 if password is acceptable, otherwise error message ID will be returned
	 */
	public boolean validatePassword(String password) {

		if (password.length() < 6) {
			return false;
		}

		return true;
	}

	public ValidationPhoneResult validatePhoneNumber(String phoneNumber) {
		ValidationPhoneResult result = new ValidationPhoneResult();

		if (phoneNumber == null || phoneNumber.length() < 9) {
			result.isValid = false;
			result.type = ValidationPhoneResult.TYPE_TOO_SHORT_PHONE_NUMBER;
			return result;
		}

		Matcher matcher = PATTERN_PHONE_NUMBER_PL.matcher(phoneNumber);
		result.isValid = matcher.matches();
		if (result.isValid == false) {
			result.type = ValidationPhoneResult.TYPE_INVALID_PHONE_NUMBER;
		}

		return result;
	}

	/**
	 * E-mail pattern seems working well :)
	 */
	private static final Pattern PATTERN_EMAIL =
			Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	/**
	 * Phone number pattern
	 */
	private static final Pattern PATTERN_PHONE_NUMBER_PL = Pattern.compile(
			"^\\s*((\\+48)?|(\\(\\+48\\))?)[-. ]?[5678]?(\\d{2})[-. ]?(\\d{3})[-. ]?(\\d{3})[-. ]?\\s*$");


	public static class ValidationPhoneResult {
		public static final int TYPE_NONE = 0;
		public static final int TYPE_TOO_SHORT_PHONE_NUMBER = 1;
		public static final int TYPE_INVALID_PHONE_NUMBER = 2;

		public int type = TYPE_NONE;
		public boolean isValid = false;
	}
}
