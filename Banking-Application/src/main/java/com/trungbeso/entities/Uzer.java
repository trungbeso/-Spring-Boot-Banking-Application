package com.trungbeso.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Uzer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String firstName;

	String lastName;

	String otherName;

	String gender;

	String address;

	String stateOfOrigin;

	String accountNumber;

	BigDecimal accountBalance;

	String email;

	String phoneNumber;

	String alternativePhoneNumber;

	String status;

	@CreationTimestamp
	LocalDateTime createdAt;

	@UpdateTimestamp
	LocalDateTime modifiedAt;
}
