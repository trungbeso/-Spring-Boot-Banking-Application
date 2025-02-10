package com.trungbeso.repositories;

import com.trungbeso.entities.Uzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUzerRepository extends JpaRepository<Uzer, Long> {
	boolean existsUzerByEmail(String email);

	boolean existsByAccountNumber(String accountNumber);

	Uzer findByAccountNumber(String accountNumber);
}
