package com.cpapijanni.password.validator;

public class LengthValidator {

	/**
	 * This method will validate the password length is in-between 5 to 12
	 * characters or not.
	 * 
	 * @param password
	 *            - Password to validate
	 * @return {@link Boolean}
	 */
	public static boolean validate(String password) {
		return password != null && (password.length() < 5 || password.length() > 12);
	}
}
