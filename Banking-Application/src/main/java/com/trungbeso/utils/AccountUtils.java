package com.trungbeso.utils;

import java.time.Year;

public class AccountUtils {
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "Account already exists";
	public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
	public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created";

	public static String generateAccountNumber() {
		Year currentYear = Year.now();
		int min = 10000;
		int max = 999999;

		// generate a random number
		int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min );

		//convert to String
		String year = String.valueOf(currentYear);
		String randNumber = String.valueOf(randomNumber);

		StringBuilder accountNumber = new StringBuilder();

		return accountNumber.append(year).append(randNumber).toString();
	}
}
