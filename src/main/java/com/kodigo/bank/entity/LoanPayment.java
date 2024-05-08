package com.kodigo.bank.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

import java.math.BigDecimal;


@Data
@Entity
@Table(name = "loanpayment")
public class LoanPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;
    private BigDecimal amount;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "loanId")
    private BankLoan bankLoan;
}