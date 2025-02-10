package com.trungbeso.repositories;

import com.trungbeso.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, String> {
}
