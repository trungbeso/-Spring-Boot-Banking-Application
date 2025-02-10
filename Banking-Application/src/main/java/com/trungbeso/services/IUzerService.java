package com.trungbeso.services;

import com.trungbeso.dtos.BankResponse;
import com.trungbeso.dtos.CreditDebitRequest;
import com.trungbeso.dtos.EnquiryRequest;
import com.trungbeso.dtos.UzerCreateRequest;

public interface IUzerService {
	BankResponse create(UzerCreateRequest request);

	BankResponse balanceEnquiry(EnquiryRequest request);

	String nameEnquiry(EnquiryRequest request);

	BankResponse creditAccount(CreditDebitRequest request);

	BankResponse debitAccount(CreditDebitRequest request);
}
