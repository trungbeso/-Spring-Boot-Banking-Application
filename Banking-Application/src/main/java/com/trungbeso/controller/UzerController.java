package com.trungbeso.controller;

import com.trungbeso.dtos.BankResponse;
import com.trungbeso.dtos.UzerCreateRequest;
import com.trungbeso.services.IUzerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
