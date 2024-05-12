package com.kodigo.bank.entity;
import jakarta.persistence.*;


import java.math.BigDecimal;

@Entity
@Table(name = "CreditCard")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @Column(name = "credit_limit", nullable = false, precision = 10, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "annual_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal annualFee;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    // Getters and setters
}