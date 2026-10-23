package com.kodigo.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "credit_card")
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_Id")
    private Long cardId;

    @Column(name = "creditLimit")
    private BigDecimal creditLimit;

    @Column(name = "annualFee")
    private BigDecimal annualFee;

    @ManyToOne
    @JoinColumn(name = "customer_Id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "bank_Id")
    private Bank bank;
}
