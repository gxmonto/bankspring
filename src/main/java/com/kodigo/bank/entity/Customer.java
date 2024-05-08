package com.kodigo.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_Id")
    private Long customerId;
    private String customerType;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "id")
    private People people;

    @OneToMany(mappedBy = "customer")
    private List<BankLoan> bankLoans;
}

