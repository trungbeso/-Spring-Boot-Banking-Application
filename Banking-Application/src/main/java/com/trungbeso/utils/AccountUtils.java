package com.trungbeso.utils;

import java.time.Year;

public class AccountUtils {
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "Account already exists";
	public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
	public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created";
	public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
	public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with provided Account number not exists";
	public static final String ACCOUNT_FOUND_CODE = "004";
	public static final String ACCOUNT_FOUND_MESSAGE = "Account found";
	public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "005";
	public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User account credited successfully";
	public static final String INSUFFICIENT_BALANCES_CODE = "006";
	public static final String INSUFFICIENT_BALANCES_MESSAGE = "Insufficient Balances";
	public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "007";
	public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account have been successfully debited";
	public static final String TRANSFER_SUCCESS_CODE = "008";
	public static final String TRANSFER_SUCCESS_MESSAGE = "Transfer successful";


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
