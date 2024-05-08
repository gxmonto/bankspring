package com.kodigo.bank.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Entity
@Table(name = "bankloan")
public class BankLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long loanId;
    private BigDecimal amount;
    private BigDecimal interest_rate;
    private long duration;

    @ManyToOne
    @JoinColumn(name = "customerId") //llave foranea a la tabla/clase Customer
    private Customer customer;
    //private long customerId;
    //private long bankId;
    @ManyToOne
    @JoinColumn(name = "bankId") //llave foranea a la tabla/clase Bank
    private Bank bank;


}