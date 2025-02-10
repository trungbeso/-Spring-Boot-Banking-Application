package com.trungbeso.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankResponse {
	@Schema(name = "Bank response code")
	String responseCode;

	@Schema(name = "Bank response message")
	String responseMessage;

	@Schema(name = "Bank response account information")
	AccountInfo accountInfo;
}
