package com.trungbeso.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountInfo {
	String accountName;

	String accountNumber;

	BigDecimal accountBalance;
}
