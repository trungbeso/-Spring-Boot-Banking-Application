package com.trungbeso.services;

import com.trungbeso.dtos.*;

public interface IUzerService {
	BankResponse create(UzerCreateRequest request);

	BankResponse login(LoginRequestDto requestDto);

	BankResponse balanceEnquiry(EnquiryRequest request);

	String nameEnquiry(EnquiryRequest request);

	BankResponse creditAccount(CreditDebitRequest request);

	BankResponse debitAccount(CreditDebitRequest request);

	BankResponse transfer(TransferRequest request);
}
