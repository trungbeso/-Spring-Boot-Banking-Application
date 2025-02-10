package com.trungbeso.services;

import com.trungbeso.dtos.*;
import com.trungbeso.entities.Uzer;
import com.trungbeso.repositories.IUzerRepository;
import com.trungbeso.utils.AccountUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class UzerService implements IUzerService{

	IUzerRepository userRepository;
	IEmailService emailService;

	@Override
	public BankResponse create(UzerCreateRequest request) {

		if (userRepository.existsUzerByEmail(request.getEmail())) {
			return BankResponse.builder()
				  .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
				  .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
				  .accountInfor(null)
				  .build();
		}


		Uzer newUser = Uzer.builder()
			  .firstName(request.getFirstName())
			  .lastName(request.getLastName())
			  .gender(request.getGender())
			  .address(request.getAddress())
			  .stateOfOrigin(request.getStateOfOrigin())
			  .email(request.getEmail())
			  .phoneNumber(request.getPhoneNumber())
			  .alternativePhoneNumber(request.getAlternativePhoneNumber())
			  .status("ACTIVE")
			  .accountNumber(AccountUtils.generateAccountNumber())
			  .accountBalance(BigDecimal.ZERO)
			  .build();

		newUser = userRepository.save(newUser);
		// send mail
		EmailDetails mailRequest = EmailDetails.builder()
			  .subject("Welcome to out system!!")
			  .recipient(newUser.getEmail())
			  .messageBody("Congratulations! Your account has been successfully created! \n Your account detail: \n" +
				    "Account name: " + newUser.getFirstName() + " " + newUser.getLastName() + " " + newUser.getOtherName()
				    +"\nAccount Number: " + newUser.getAccountNumber())
			  .build();

		emailService.sendEmailAlert(mailRequest);

		return BankResponse.builder()
			  .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
			  .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
			  .accountInfor(AccountInfo.builder()
				    .accountBalance(newUser.getAccountBalance())
				    .accountNumber(newUser.getAccountNumber())
				    .accountName(newUser.getFirstName()+ " " + newUser.getLastName()+ " " + newUser.getOtherName())
				    .build())
			  .build();
	}
	//balance Enquiry, name Enquiry, credit, debit, transfer
	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		//check if the provider account number exists in databse
		boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder()
				  .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
				  .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
				  .accountInfor(null)
				  .build();
		}
		Uzer foundedUser = userRepository.findByAccountNumber(request.getAccountNumber());
		return BankResponse.builder()
			  .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
			  .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
			  .accountInfor(AccountInfo.builder()
				    .accountName(foundedUser.getFirstName() + " " + foundedUser.getLastName()+ " " + foundedUser.getOtherName())
				    .accountNumber(foundedUser.getAccountNumber())
				    .accountBalance(foundedUser.getAccountBalance())
				    .build())
			  .build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
		}
		Uzer foundedUser = userRepository.findByAccountNumber(request.getAccountNumber());
		return foundedUser.getFirstName() + " " + foundedUser.getLastName() + " " + foundedUser.getOtherName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {
		// check if the account exists
		boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder()
				  .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
				  .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
				  .accountInfor(null)
				  .build();
		}

		Uzer userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		userToCredit = userRepository.save(userToCredit);

		return BankResponse.builder()
			  .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
			  .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
			  .accountInfor(AccountInfo.builder()
				    .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName()+ " " + userToCredit.getOtherName())
				    .accountNumber(userToCredit.getAccountNumber())
				    .accountBalance(userToCredit.getAccountBalance())
				    .build())
			  .build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {
		//check account exists
		boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder()
				  .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
				  .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
				  .accountInfor(null)
				  .build();
		}

		//check if the amount you intend to withdraw not more than the current balance
		Uzer userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
		BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
		BigInteger debitAmount = request.getAmount().toBigInteger();
		if (availableBalance.intValue() < debitAmount.intValue()) {
			return BankResponse.builder()
				  .responseCode(AccountUtils.INSUFFICIENT_BALANCES_CODE)
				  .responseMessage(AccountUtils.INSUFFICIENT_BALANCES_MESSAGE)
				  .accountInfor(null)
				  .build();
		} else {
			userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
			userToDebit = userRepository.save(userToDebit);
			return BankResponse.builder()
				  .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
				  .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
				  .accountInfor(AccountInfo.builder()
					    .accountNumber(request.getAccountNumber())
					    .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName()+ " " + userToDebit.getOtherName())
					    .accountBalance(userToDebit.getAccountBalance())
					    .build())
				  .build();
		}

	}


}
