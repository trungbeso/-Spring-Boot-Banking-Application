package com.trungbeso.controller;

import com.trungbeso.dtos.BankResponse;
import com.trungbeso.dtos.CreditDebitRequest;
import com.trungbeso.dtos.EnquiryRequest;
import com.trungbeso.dtos.UzerCreateRequest;
import com.trungbeso.services.IUzerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UzerController {

	IUzerService uzerService;

	@PostMapping
	public BankResponse createAccount(@RequestBody UzerCreateRequest request) {
		return uzerService.create(request);
	}

	@GetMapping("/balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
		return uzerService.balanceEnquiry(request);
	}

	@GetMapping("/nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return uzerService.nameEnquiry(request);
	}

	@PostMapping("/credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return uzerService.creditAccount(request);
	}

	@PostMapping("/debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
		return uzerService.debitAccount(request);
	}
}
