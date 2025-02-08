package com.trungbeso.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UzerCreateRequest {
	String firstName;

	String lastName;

	String otherName;

	String gender;

	String address;

	String stateOfOrigin;

	String email;

	String phoneNumber;

	String alternativePhoneNumber;

	String status;
}
