package com.kodigo.bank.repository;

import com.kodigo.bank.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long>{
}
