package com.trungbeso.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankResponse {
	String responseCode;

	String responseMessage;

	AccountInfo accountInfor;
}
