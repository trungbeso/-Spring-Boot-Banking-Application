package com.trungbeso.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionDto {
	String transactionType;

	BigDecimal amount;

	String accountNumber;

	String status;
}
