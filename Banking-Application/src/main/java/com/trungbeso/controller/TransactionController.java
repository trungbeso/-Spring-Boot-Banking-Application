package com.trungbeso.controller;

import com.itextpdf.text.DocumentException;
import com.trungbeso.entities.Transaction;
import com.trungbeso.services.BankStatement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/bankStatement")
@AllArgsConstructor
public class TransactionController {

	private BankStatement bankStatement;

	@GetMapping
	public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
	                                               @RequestParam String startDate,
	                                               @RequestParam String endDate) throws DocumentException, FileNotFoundException {
		return bankStatement.generateTransactions(accountNumber, startDate, endDate);
	}
}
