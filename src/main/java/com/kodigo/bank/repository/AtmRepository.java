package com.kodigo.bank.repository;

import com.kodigo.bank.entity.Atm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtmRepository extends JpaRepository<Atm, Long> {
    Optional<Atm> findByAtmCode(String atmCode);
}
