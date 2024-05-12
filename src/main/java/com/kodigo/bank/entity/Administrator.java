package com.kodigo.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "Administrator")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "administrator_Id")
    private Long administratorId;

    @ManyToOne
    @JoinColumn(name = "id")
    private People people;

    @OneToMany(mappedBy = "administrator")
    private List<BankLoan> bankLoans;
}

