package com.kodigo.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Codigo de retiro sin tarjeta. El codigo en si nunca se guarda, solo su hash:
// el cliente lo ve una vez al generarlo y luego solo se muestran los ultimos 2 digitos.
@Data
@Entity
@Table(name = "withdrawal_code")
public class WithdrawalCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(name = "code_hash", unique = true, nullable = false, length = 64)
    private String codeHash;

    @Column(name = "code_hint", nullable = false, length = 2)
    private String codeHint;

    private BigDecimal amount;

    // Se guarda como texto (VARCHAR), igual en MySQL y en H2
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 20)
    private WithdrawalStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime redeemedAt;

    // Cajero donde se cobro
    private String atmCode;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Un codigo ACTIVO cuyo tiempo ya paso se muestra como EXPIRED
    public String getDisplayStatus() {
        if (status == WithdrawalStatus.ACTIVE && expiresAt.isBefore(LocalDateTime.now())) {
            return "EXPIRED";
        }
        return status.name();
    }
}
