package com.trungbeso.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDetails {
	String recipient;

	String messageBody;

	String subject;

	String attachment;
}
