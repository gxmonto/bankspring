package com.kodigo.bank.repository;

import com.kodigo.bank.entity.WithdrawalCode;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawalCodeRepository extends JpaRepository<WithdrawalCode, Long> {
    boolean existsByCodeHash(String codeHash);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<WithdrawalCode> findByCodeHash(String codeHash);

    List<WithdrawalCode> findTop20ByCustomerCustomerIdOrderByCreatedAtDesc(Long customerId);

    Optional<WithdrawalCode> findByIdAndCustomerCustomerId(Long id, Long customerId);

    // Dinero reservado por codigos que todavia se pueden cobrar
    @Query("SELECT COALESCE(SUM(w.amount), 0) FROM WithdrawalCode w WHERE w.customer.customerId = :customerId " +
            "AND w.status = com.kodigo.bank.entity.WithdrawalStatus.ACTIVE AND w.expiresAt > :now")
    BigDecimal sumReserved(@Param("customerId") Long customerId, @Param("now") LocalDateTime now);
}
