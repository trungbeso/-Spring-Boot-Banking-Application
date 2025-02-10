package com.trungbeso.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountInfo {

	@Schema(name = "User Account Name")
	String accountName;

	@Schema(name = "User Account Number")
	String accountNumber;

	@Schema(name = "User Account Balance")
	BigDecimal accountBalance;
}
