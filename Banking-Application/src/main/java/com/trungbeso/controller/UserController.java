package com.trungbeso.controller;

import com.trungbeso.dtos.*;
import com.trungbeso.services.IUzerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "User Account Management APIs")
public class UserController {

	IUzerService userService;

	@Operation(summary = "Create new user account", description = "Creating a new user and assigning an account ID")
	@ApiResponse(responseCode = "201", description = "Http Status 201 CREATED")
	@PostMapping
	public BankResponse createAccount(@RequestBody UzerCreateRequest request) {
		return userService.create(request);
	}

	@Operation(summary = "Balance Enquiry", description = "Given an account number, check how much the user has")
	@ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
	@GetMapping("/balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
		return userService.balanceEnquiry(request);
	}

	@Operation(summary = "Name Enquiry", description = "Given an account number, check what is user name")
	@ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
	@GetMapping("/nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return userService.nameEnquiry(request);
	}

	@Operation(summary = "Credit Account", description = "Add money to the user's account")
	@ApiResponse(responseCode = "005", description = "User account credited successfully")
	@PostMapping("/credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return userService.creditAccount(request);
	}

	@Operation(summary = "Debit Account", description = "Subtract money from the user's account")
	@ApiResponse(responseCode = "007", description = "Account have been successfully debited")
	@PostMapping("/debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
		return userService.debitAccount(request);
	}

	@Operation(summary = "Transfer", description = "Transfer money from source account to destination account")
	@ApiResponse(responseCode = "008", description = "Transfer successful")
	@PostMapping("/transfer")
	public BankResponse transferAccount(@RequestBody TransferRequest request) {
		return userService.transfer(request);
	}
}
