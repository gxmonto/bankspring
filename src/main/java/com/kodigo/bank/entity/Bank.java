package com.kodigo.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Bank")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_Id")
    private Long bankId;
    private String bankName;
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "bank")
    private List<BankLoan> bankLoans;
}
