package com.trungbeso.services;

import com.trungbeso.dtos.AccountInfo;
import com.trungbeso.dtos.BankResponse;
import com.trungbeso.dtos.UzerCreateRequest;
import com.trungbeso.entities.Uzer;
import com.trungbeso.repositories.IUzerRepository;
import com.trungbeso.utils.AccountUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class UzerService implements IUzerService{

	IUzerRepository userRepository;

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
}
