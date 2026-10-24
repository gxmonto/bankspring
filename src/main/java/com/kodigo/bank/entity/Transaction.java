package com.kodigo.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Cada movimiento de dinero de una cuenta. Nunca se edita ni se borra: es el historial.
@Data
@NoArgsConstructor // JPA necesita un constructor vacio para leer registros
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    // Se guarda como texto (VARCHAR), igual en MySQL y en H2
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 20)
    private TransactionType type;

    private BigDecimal amount;

    // Saldo de la cuenta justo despues de este movimiento, como en un estado de cuenta
    private BigDecimal balanceAfter;

    private String description;

    private LocalDateTime date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Transaction(TransactionType type, BigDecimal amount, BigDecimal balanceAfter, String description, Customer customer) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.date = LocalDateTime.now();
        this.customer = customer;
    }
}
