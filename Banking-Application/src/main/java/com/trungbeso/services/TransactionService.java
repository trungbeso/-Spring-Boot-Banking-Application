package com.trungbeso.services;

import com.trungbeso.dtos.TransactionDto;
import com.trungbeso.entities.Transaction;
import com.trungbeso.repositories.ITransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class TransactionService implements ITransactionService{

	ITransactionRepository repository;

	@Override
	public void save(TransactionDto transactionDto) {
		Transaction transaction = Transaction.builder()
			  .transactionType(transactionDto.getTransactionType())
			  .amount(transactionDto.getAmount())
			  .status("SUCCESS")
			  .accountNumber(transactionDto.getAccountNumber())
			  .build();

		transaction = repository.save(transaction);
		System.out.println("Transaction saved successfully");
	}
}
