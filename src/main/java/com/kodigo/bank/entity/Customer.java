package com.kodigo.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "people_id")
    private People people;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<BankLoan> bankLoans;
}
