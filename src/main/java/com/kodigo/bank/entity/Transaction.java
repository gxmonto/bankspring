package com.kodigo.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

// Transaction.java
@Data
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_Id")
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "customer_Id")
    private Customer customer;

    // Add a constructor that takes the required parameters
    public Transaction(TransactionType type, BigDecimal amount, Date date, Customer customer) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.customer = customer;
    }
}