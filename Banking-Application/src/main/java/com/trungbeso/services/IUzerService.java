package com.trungbeso.services;

import com.trungbeso.dtos.BankResponse;
import com.trungbeso.dtos.UzerCreateRequest;

public interface IUzerService {
	BankResponse create(UzerCreateRequest request);


}
