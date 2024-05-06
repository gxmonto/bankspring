package com.kodigo.bank.repository;

import com.kodigo.bank.entity.BankLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankLoanRepository extends JpaRepository<BankLoan, Long> {
}
