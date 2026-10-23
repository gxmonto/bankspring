package com.kodigo.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "card_payment")
public class CardPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_Id")
    private Long paymentId;

    private BigDecimal amount;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "card_Id")
    private CreditCard creditCard;
}
