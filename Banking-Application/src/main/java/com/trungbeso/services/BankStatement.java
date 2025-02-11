package com.trungbeso.services;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.trungbeso.dtos.EmailDetails;
import com.trungbeso.entities.Transaction;
import com.trungbeso.entities.Uzer;
import com.trungbeso.repositories.ITransactionRepository;
import com.trungbeso.repositories.IUzerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Phaser;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BankStatement {

	ITransactionRepository transactionRepository;
	IUzerRepository userRepository;
	IEmailService emailService;

	@NonFinal
	private static String FILE = "D:\\Git Hub Repo\\My PDF sample";
	/*
	 * retrieve list of transaction within a date range given an account number
	 * generate a pdf file of transaction
	 * send the file via email
	 * */

	public List<Transaction> generateTransactions(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {

		LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE);
		List<Transaction> transactions =
			  transactionRepository.findAll()
					 .stream()
					 .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
					 .filter(transaction -> transaction.getCreatedAt().isEqual(start))
					 .filter(transaction -> transaction.getCreatedAt().isEqual(end))
					 .toList();

		Uzer user = userRepository.findByAccountNumber(accountNumber);
		String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();

		Rectangle statementSize = new Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("setting size of document");
		OutputStream outputStream = new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();

		PdfPTable bankInfoTable = new PdfPTable(1);
		PdfPCell bankName = new PdfPCell(new Phrase("TrungBeso Banking Application"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.CYAN);
		bankName.setPadding(20f);

		PdfPCell bankAddress = new PdfPCell(new Phrase("198 Tran Binh, My Dinh, Ha Noi"));
		bankAddress.setBorder(0);
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAddress);

		PdfPTable statementInfo = new PdfPTable(2);
		PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
		customerInfo.setBorder(0);

		PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);

		PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
		stopDate.setBorder(0);

		PdfPCell name = new PdfPCell(new Phrase("Customer name: " + customerName));
		name.setBorder(0);

		PdfPCell space = new PdfPCell();

		PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
		address.setBorder(0);

		PdfPTable transactionTable = new PdfPTable(4);
		PdfPCell date = new PdfPCell(new Phrase("DATE"));
		date.setBorder(0);
		date.setBackgroundColor(BaseColor.CYAN);

		PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
		transactionType.setBorder(0);
		transactionType.setBackgroundColor(BaseColor.CYAN);

		PdfPCell amount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
		amount.setBorder(0);
		amount.setBackgroundColor(BaseColor.CYAN);

		PdfPCell status = new PdfPCell(new Phrase("TRANSACTION STATUS"));
		status.setBorder(0);
		status.setBackgroundColor(BaseColor.CYAN);

		transactionTable.addCell(date);
		transactionTable.addCell(transactionType);
		transactionTable.addCell(amount);
		transactionTable.addCell(status);

		transactions.forEach(transaction -> {
			transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionTable.addCell(new Phrase(transaction.getTransactionType()));
			transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionTable.addCell(new Phrase(transaction.getStatus()));
		});

		statementInfo.addCell(customerInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(stopDate);
		statementInfo.addCell(name);
		statementInfo.addCell(space);
		statementInfo.addCell(address);

		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionTable);

		document.close();

		EmailDetails request = EmailDetails.builder()
			  .recipient(user.getEmail())
			  .subject("Statement of Account")
			  .messageBody("Kindly find your requested account statement attached!")
			  .attachment(FILE)
			  .build();

		emailService.sendEmailWithAttachment(request);

		return transactions;
	}

}
