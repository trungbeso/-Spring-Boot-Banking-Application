package com.trungbeso.services;

import com.trungbeso.configuration.JwtTokenProvider;
import com.trungbeso.dtos.*;
import com.trungbeso.entities.Role;
import com.trungbeso.entities.Uzer;
import com.trungbeso.repositories.IUzerRepository;
import com.trungbeso.utils.AccountUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class UzerService implements IUzerService{

	IUzerRepository userRepository;
	IEmailService emailService;
	ITransactionService transactionService;
	PasswordEncoder passwordEncoder;
	AuthenticationManager authenticationManager;
	JwtTokenProvider jwtTokenProvider;

	@Override
	public BankResponse create(UzerCreateRequest request) {

		if (userRepository.existsUzerByEmail(request.getEmail())) {
			return BankResponse.builder()
				  .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
				  .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
				  .accountInfo(null)
				  .build();
		}

		Uzer newUser = Uzer.builder()
			  .firstName(request.getFirstName())
			  .lastName(request.getLastName())
			  .gender(request.getGender())
			  .address(request.getAddress())
			  .stateOfOrigin(request.getStateOfOrigin())
			  .email(request.getEmail())
			  .password(passwordEncoder.encode(request.getPassword()))
			  .phoneNumber(request.getPhoneNumber())
			  .alternativePhoneNumber(request.getAlternativePhoneNumber())
			  .status("ACTIVE")
			  .role(Role.ADMIN)
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
			  .accountInfo(AccountInfo.builder()
				    .accountBalance(newUser.getAccountBalance())
				    .accountNumber(newUser.getAccountNumber())
				    .accountName(newUser.getFirstName()+ " " + newUser.getLastName()+ " " + newUser.getOtherName())
				    .build())
			  .build();
	}

	@Override
	public BankResponse login(LoginRequestDto requestDto) {
		Authentication authentication = null;
		authentication = authenticationManager.authenticate(
			  new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
		);
		return BankResponse.builder()
			  .responseCode("Login Successful")
			  .responseMessage(jwtTokenProvider.generateToken(authentication))
			  .build();
	}



	//balance Enquiry, name Enquiry, credit, debit, transfer
	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		//check if the provider account number exists in database
		boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder()
				  .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
				  .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
				  .accountInfo(null)
				  .build();
		}
		Uzer foundedUser = userRepository.findByAccountNumber(request.getAccountNumber());
		return BankResponse.builder()
			  .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
			  .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
			  .accountInfo(AccountInfo.builder()
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
				  .accountInfo(null)
				  .build();
		}

		Uzer userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		userToCredit = userRepository.save(userToCredit);

		//save transaction
		TransactionDto transactionDto = TransactionDto.builder()
			  .accountNumber(userToCredit.getAccountNumber())
			  .transactionType("CREDIT")
			  .amount(request.getAmount())
			  .build();
		transactionService.save(transactionDto);

		return BankResponse.builder()
			  .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
			  .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
			  .accountInfo(AccountInfo.builder()
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
				  .accountInfo(null)
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
				  .accountInfo(null)
				  .build();
		} else {
			userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
			userToDebit = userRepository.save(userToDebit);
			//save transaction
			TransactionDto transactionDto = TransactionDto.builder()
				  .accountNumber(userToDebit.getAccountNumber())
				  .transactionType("DEBIT")
				  .amount(request.getAmount())
				  .build();
			transactionService.save(transactionDto);

			return BankResponse.builder()
				  .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
				  .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
				  .accountInfo(AccountInfo.builder()
					    .accountNumber(request.getAccountNumber())
					    .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName()+ " " + userToDebit.getOtherName())
					    .accountBalance(userToDebit.getAccountBalance())
					    .build())
				  .build();
		}

	}

	@Override
	public BankResponse transfer(TransferRequest request) {
		//get the account to debit
		//check if the amount I'm debiting is not more than current balance
		//debit the account
		//get the account to credit
		// the account

		boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
		if (!isDestinationAccountExists) {
			return BankResponse.builder()
				  .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
				  .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
				  .accountInfo(null)
				  .build();
		}

		Uzer sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
		if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) < 0) {
			return BankResponse.builder()
				  .responseCode(AccountUtils.INSUFFICIENT_BALANCES_CODE)
				  .responseMessage(AccountUtils.INSUFFICIENT_BALANCES_MESSAGE)
				  .accountInfo(null)
				  .build();
		}

		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
		String sourceUsername = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName() + " " + sourceAccountUser.getOtherName();

		sourceAccountUser = userRepository.save(sourceAccountUser);
		EmailDetails debitAlert = EmailDetails.builder()
			  .subject("Debit alert")
			  .recipient(sourceAccountUser.getEmail())
			  .messageBody("The sum of " + request.getAmount() + " has been deducted from your account! Your current " +
				    "balance is " + sourceAccountUser.getAccountBalance())
			  .build();

		emailService.sendEmailAlert(debitAlert);

		Uzer destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
		String recipientUsername = destinationAccountUser.getFirstName() + " " + destinationAccountUser.getLastName() + " " + destinationAccountUser.getOtherName();
		destinationAccountUser = userRepository.save(destinationAccountUser);
		EmailDetails creditAlert = EmailDetails.builder()
			  .subject("Credit alert")
			  .recipient(sourceAccountUser.getEmail())
			  .messageBody("The sum of " + request.getAmount() + " has been sent to your account from " + sourceUsername +
				    " your current balance is: " + sourceAccountUser.getAccountBalance())
			  .build();
		emailService.sendEmailAlert(creditAlert);
		//save transaction
		TransactionDto transactionDto = TransactionDto.builder()
			  .accountNumber("Source Account: " + sourceAccountUser.getAccountNumber() + "\n Destination Account: " + destinationAccountUser.getAccountNumber())
			  .transactionType("Transfer")
			  .amount(request.getAmount())
			  .build();
		transactionService.save(transactionDto);

		return BankResponse.builder()
			  .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
			  .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
			  .accountInfo(null)
			  .build();

	}


}
