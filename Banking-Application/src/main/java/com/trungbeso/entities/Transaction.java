package com.trungbeso.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String transactionId;

	String transactionType;

	BigDecimal amount;

	String accountNumber;

	String status;
}
