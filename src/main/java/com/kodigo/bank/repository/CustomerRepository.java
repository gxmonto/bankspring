package com.kodigo.bank.repository;

import com.kodigo.bank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c.balance FROM Customer c WHERE c.customerId = :customerId")
    BigDecimal getBalance(@Param("customerId") Long customerId);

    Optional<Customer> findFirstByPeopleUsername(String username);

    Optional<Customer> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);
}
